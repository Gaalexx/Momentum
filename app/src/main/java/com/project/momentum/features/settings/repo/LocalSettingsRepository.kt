package com.project.momentum.features.settings.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.project.momentum.di.LocalPrefs
import com.project.momentum.features.settings.models.dto.LocalSettingsStateDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsLocalRepository @Inject constructor(
    @LocalPrefs private val dataStore: DataStore<Preferences>,
) {

    private object Keys {
        val CONFIRM_BEFORE_POST = booleanPreferencesKey("confirm_before_post")
    }

    suspend fun setConfirmBeforePost(newValue: Boolean): Result<LocalSettingsStateDTO> =
        runCatching {
            dataStore.edit { it[Keys.CONFIRM_BEFORE_POST] = newValue }
            LocalSettingsStateDTO(newValue)
        }
}
