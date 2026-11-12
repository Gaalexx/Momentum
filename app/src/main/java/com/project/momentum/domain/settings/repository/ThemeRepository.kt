package com.project.momentum.domain.settings.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val isDefaultTheme: Flow<Boolean>

    suspend fun setDefaultTheme(enabled: Boolean)
}
