package com.project.momentum.domain.settings.repository

import com.project.momentum.domain.settings.model.ServerSettings

interface ServerSettingsRepository {
    suspend fun changeInAppNotifications(newValue: Boolean): Result<ServerSettings>
    suspend fun changePublicationsEnabled(newValue: Boolean): Result<ServerSettings>
    suspend fun changeReactionsEnabled(newValue: Boolean): Result<ServerSettings>
    suspend fun changeFriendRequestEnabled(newValue: Boolean): Result<ServerSettings>
    suspend fun changeDefaultThemeEnabled(newValue: Boolean): Result<ServerSettings>
    suspend fun getServerSettingsInfo(): Result<ServerSettings>
}
