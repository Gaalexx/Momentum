package com.project.momentum.presentation

import com.project.momentum.R

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import com.project.momentum.presentation.navigation.MainScreen
import com.project.momentum.presentation.common.theme.MomentumTheme
import com.project.momentum.presentation.common.theme.MomentumThemeVariant
import com.project.momentum.presentation.common.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeManager: ThemeManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showNotificationsDisabledDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        askNotificationPermission()
        setContent {
            val isDefaultTheme by themeManager.isDefaultTheme.collectAsState()

            MomentumTheme(
                themeVariant = if (isDefaultTheme) MomentumThemeVariant.Momentum else MomentumThemeVariant.AndroidSettings
            ) {
                MainScreen(themeManager = themeManager)
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
