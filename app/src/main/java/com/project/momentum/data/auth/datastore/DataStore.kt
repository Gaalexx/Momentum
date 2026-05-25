package com.project.momentum.data.auth.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.project.momentum.data.auth.keystore.EncryptedData
import com.project.momentum.di.AuthPrefs
import com.project.momentum.di.ThemePrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton



interface AuthStorage {
    suspend fun saveEncryptedData(encryptedData: EncryptedData)
    suspend fun getEncryptedData(): EncryptedData?
    suspend fun clear()
}


@Singleton
class AuthStorageImpl @Inject constructor(
    @AuthPrefs private val dataStore: DataStore<Preferences>
) : AuthStorage {

    private companion object {
        val ENCRYPTED_DATA = stringPreferencesKey("encrypted_data")
    }

    override suspend fun saveEncryptedData(encryptedData: EncryptedData) {
        val json = Json.encodeToString(encryptedData)
        dataStore.edit { prefs ->
            prefs[ENCRYPTED_DATA] = json
        }
    }

    override suspend fun getEncryptedData(): EncryptedData? {
        val json = dataStore.data.first()[ENCRYPTED_DATA] ?: return null
        return Json.decodeFromString<EncryptedData>(json)
    }

    override suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(ENCRYPTED_DATA)
        }
    }
}

@Singleton
class ThemeDataStore @Inject constructor(
    @ThemePrefs private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val IS_DEFAULT_THEME = booleanPreferencesKey("is_default_theme")
    }

    val isDefaultThemeFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_DEFAULT_THEME] ?: true
        }

    suspend fun setDefaultTheme(isDefault: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DEFAULT_THEME] = isDefault
        }
    }
}