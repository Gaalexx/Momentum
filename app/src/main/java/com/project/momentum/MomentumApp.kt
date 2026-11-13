package com.project.momentum

import android.app.Application
import com.vk.id.VKID
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MomentumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        VKID.init(this)
        VKID.instance.setLocale(Locale.forLanguageTag("ru"))
    }
}
