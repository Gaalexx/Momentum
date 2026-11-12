package com.project.momentum.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.project.momentum.di.ThemePrefs
import com.project.momentum.domain.settings.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    @ThemePrefs private val dataStore: DataStore<Preferences>,
) : ThemeRepository {
    override val isDefaultTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DEFAULT_THEME] ?: true
    }

    override suspend fun setDefaultTheme(enabled: Boolean) {
        dataStore.edit { it[IS_DEFAULT_THEME] = enabled }
    }

    private companion object {
        val IS_DEFAULT_THEME = booleanPreferencesKey("is_default_theme")
    }
}
