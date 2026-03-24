package com.project.momentum

import android.app.Application
import com.project.momentum.features.settings.repo.AppSettingsHolder
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MomentumApp : Application(){
    @Inject
    lateinit var appSettingsHolder: AppSettingsHolder

    override fun onCreate() {
        super.onCreate()

        // ВАЖНО: это заставляет Hilt создать Singleton прямо сейчас
        appSettingsHolder.toString()
    }
}
