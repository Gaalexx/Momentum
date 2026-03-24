package com.project.momentum.features.settings.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.project.momentum.di.ApplicationScope
import com.project.momentum.di.LocalPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsHolder @Inject constructor(
    @LocalPrefs private val dataStore: DataStore<Preferences>,
    @ApplicationScope private val scope: CoroutineScope
) {

    private object Keys {
        val CONFIRM_BEFORE_POST = booleanPreferencesKey("confirm_before_post")
    }

    val confirmBeforePost: StateFlow<Boolean> =
        dataStore.data
            .map { prefs -> prefs[Keys.CONFIRM_BEFORE_POST] ?: false }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

}
