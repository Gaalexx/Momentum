package com.project.momentum

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.project.momentum.features.auth.viewmodel.AuthorizationViewModel
import com.project.momentum.navigation.MainScreen
import com.project.momentum.navigation.viewmodel.AppStartViewModel
import com.project.momentum.ui.theme.MomentumAndroidSettingsTheme
import com.vk.api.sdk.VK
import com.vk.dto.common.id.toUserId
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private companion object {
        val VKID_AUTH_SCOPES = setOf("email", "friends", "photos", "video")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showNotificationsDisabledDialog()
        }
    }
    private val authViewModel: AuthorizationViewModel by viewModels()
    private val appStartViewModel: AppStartViewModel by viewModels()
    private val vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            applyVkSdkCredentials(accessToken)
            Log.d(
                "VkAuth",
                "VKID auth success: userId=${accessToken.userID}, last name=${accessToken.userData.lastName}",
            )

            authViewModel.onVkAuth(accessToken) {
                appStartViewModel.onVKAuthSuccess()
            }
        }

        override fun onFail(fail: VKIDAuthFail) {
            Log.e(
                "VkAuth",
                "VKID auth failed: type=${fail.javaClass.simpleName}, description=${fail.description}",
                fail.toThrowable(),
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        askNotificationPermission()
        VKID.instance.accessToken?.let { applyVkSdkCredentials(it) }
        setContent {
            MomentumAndroidSettingsTheme {
                MainScreen(
                    onVkAuth = ::startVkAuth
                )
            }
        }
    }

    private fun startVkAuth() {
        VKID.instance.authorize(
            lifecycleOwner = this,
            callback = vkAuthCallback,
            params = VKIDAuthParams {
                scopes = VKID_AUTH_SCOPES
                useOAuthProviderIfPossible = false
                oAuth = OAuth.VK
            },
        )
    }

    private fun applyVkSdkCredentials(accessToken: AccessToken) {
        val createdMs = System.currentTimeMillis()
        val expiresInSec = accessToken.toVkSdkExpiresInSec(createdMs)

        VK.setCredentials(
            accessToken.userID.toUserId(),
            accessToken.token,
            null,
            expiresInSec,
            createdMs,
            true,
        )

        Log.d("VkAuth", "VK SDK credentials applied from VKID token: expiresInSec=$expiresInSec")
    }

    private fun AccessToken.toVkSdkExpiresInSec(createdMs: Long): Int =
        if (expireTime > createdMs) {
            ((expireTime - createdMs) / 1000L)
                .coerceAtMost(Int.MAX_VALUE.toLong())
                .toInt()
        } else {
            -1
        }

    private fun VKIDAuthFail.toThrowable(): Throwable? =
        when (this) {
            is VKIDAuthFail.FailedApiCall -> throwable
            is VKIDAuthFail.FailedRedirectActivity -> throwable
            is VKIDAuthFail.NoBrowserAvailable -> throwable
            else -> null
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED -> Unit

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.notification_permission_title)
                    .setMessage(R.string.notification_permission_message)
                    .setPositiveButton(R.string.button_ok) { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .setNegativeButton(R.string.button_no_thanks, null)
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showNotificationsDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.notifications_disabled_title)
            .setMessage(R.string.notifications_disabled_message)
            .setPositiveButton(R.string.button_ok, null)
            .show()
    }
}
