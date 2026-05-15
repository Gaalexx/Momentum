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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.features.auth.viewmodel.AuthorizationViewModel
import com.project.momentum.features.contentcreation.viewmodel.UploadState
import com.project.momentum.navigation.MainScreen
import com.project.momentum.ui.theme.MomentumAndroidSettingsTheme
import com.project.momentum.ui.theme.MomentumTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showNotificationsDisabledDialog()
        }
    }
    private lateinit var launcher: ActivityResultLauncher<Collection<VKScope>>
    private val authViewModel: AuthorizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = VK.login(this) {
            result ->
            when (result) {
                is VKAuthenticationResult.Failed -> { //TODO: обработка ошибок
                    Log.e("VkAuth", "Ошибка входа: ${result.exception.toString()}")
                }
                is VKAuthenticationResult.Success -> {
                    Log.d("VkAuth", "Token info: ${result.token}")
                    authViewModel.onVkAuth(result.token)
                }
            }
        }

        enableEdgeToEdge()
        askNotificationPermission()
        setContent {
            MomentumAndroidSettingsTheme() {
                MainScreen(
                    onVkAuth = {
                        launcher.launch(listOf(
                                VKScope.EMAIL,
                                VKScope.PHONE,
                                VKScope.NOTIFICATIONS,
                                VKScope.FRIENDS,
                                VKScope.PHOTOS,
                                VKScope.VIDEO,
                            )
                        )
                    }
                )
            }
        }
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
