package com.project.momentum.data.auth.deviceinfo

import android.annotation.SuppressLint
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext


@Singleton
class DeviceInfo @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private data class DeviceSessionInfo(
        val manufacturer: String,
        val model: String,
        val brand: String,
        val device: String,
        val product: String,
        val hardware: String,
        val board: String,
        val androidVersion: String,
        val sdkInt: Int,
        val fingerprint: String,
        val supportedAbis: List<String>,
        val androidId: String
    )

    @SuppressLint("HardwareIds")
    private fun getDeviceSessionInfo(): DeviceSessionInfo {
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"

        return DeviceSessionInfo(
            manufacturer = Build.MANUFACTURER.orEmpty(),
            model = Build.MODEL.orEmpty(),
            brand = Build.BRAND.orEmpty(),
            device = Build.DEVICE.orEmpty(),
            product = Build.PRODUCT.orEmpty(),
            hardware = Build.HARDWARE.orEmpty(),
            board = Build.BOARD.orEmpty(),
            androidVersion = Build.VERSION.RELEASE.orEmpty(),
            sdkInt = Build.VERSION.SDK_INT,
            fingerprint = Build.FINGERPRINT.orEmpty(),
            supportedAbis = Build.SUPPORTED_ABIS?.toList().orEmpty(),
            androidId = androidId
        )
    }

    fun getPhoneInfo(): String =
        getDeviceSessionInfo().let { "${it.brand} ${it.model} ${it.device}" }

}