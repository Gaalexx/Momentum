package com.project.momentum

import android.app.Application
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.settings.repo.AppSettingsHolder
import com.vk.api.sdk.VK
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MomentumApp : Application() {
    @Inject
    lateinit var appSettingsHolder: AppSettingsHolder

    override fun onCreate() {
        super.onCreate()
        appSettingsHolder.toString()
        VK.initialize(this)

    }
}
