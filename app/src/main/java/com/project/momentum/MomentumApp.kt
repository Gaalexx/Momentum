package com.project.momentum

import android.app.Application
import com.project.momentum.features.settings.repo.AppSettingsHolder
import com.vk.api.sdk.VK
import com.vk.id.VKID
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
        VKID.init(this)
        VKID.instance.setLocale(Locale.forLanguageTag("ru"))
    }
}
