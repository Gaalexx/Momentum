package com.project.momentum.data.auth.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.momentum.data.auth.keystore.EncryptedData
import com.project.momentum.di.AuthPrefs
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

val Context.localSettingsDataStore by preferencesDataStore(
    name = "local_settings"
)


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

