package com.project.momentum.features.settings.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.project.momentum.di.LocalPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsLocalRepository @Inject constructor(
    @LocalPrefs private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val CONFIRM_BEFORE_POST = booleanPreferencesKey("confirm_before_post")
    }

    val confirmBeforePost: Flow<Boolean> =
        dataStore.data.map { it[Keys.CONFIRM_BEFORE_POST] ?: false }

    suspend fun setConfirmBeforePost(newValue: Boolean): Result<Boolean> =
        runCatching {
            dataStore.edit { it[Keys.CONFIRM_BEFORE_POST] = newValue }
            newValue
        }


}
