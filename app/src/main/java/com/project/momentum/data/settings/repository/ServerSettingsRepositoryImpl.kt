package com.project.momentum.data.settings.repository

import com.project.momentum.data.common.cancellableRunCatching

import com.project.momentum.data.settings.remote.GetSettingsInfoAPI
import com.project.momentum.data.settings.remote.SettingsMainAPI
import com.project.momentum.data.settings.mapper.toDomain
import com.project.momentum.domain.settings.model.ServerSettings
import com.project.momentum.domain.settings.repository.ServerSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerSettingsRepositoryImpl @Inject constructor(
    private val client: SettingsMainAPI,
    private val settingsInfoAPI: GetSettingsInfoAPI,
) : ServerSettingsRepository {
    override suspend fun changeInAppNotifications(newValue: Boolean) : Result<ServerSettings> =
        cancellableRunCatching {
            val response = client.changeInAppNotifications(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState?.toDomain() ?: throw Exception("Server returned no state")
        }

    override suspend fun changePublicationsEnabled(newValue: Boolean) : Result<ServerSettings> =
        cancellableRunCatching {
            val response = client.changePublicationsEnabled(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState?.toDomain() ?: throw Exception("Server returned no state")
        }

    override suspend fun changeReactionsEnabled(newValue: Boolean) : Result<ServerSettings> =
        cancellableRunCatching {
            val response = client.changeReactionsEnabled(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState?.toDomain() ?: throw Exception("Server returned no state")
        }

    override suspend fun changeFriendRequestEnabled(newValue: Boolean) : Result<ServerSettings> =
        cancellableRunCatching {
            val response = client.changeFriendRequestEnabled(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState?.toDomain() ?: throw Exception("Server returned no state")
        }

    override suspend fun changeDefaultThemeEnabled(newValue: Boolean) : Result<ServerSettings> =
        cancellableRunCatching {
            val response = client.changeDefaultThemeEnabled(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState?.toDomain() ?: throw Exception("Server returned no state")
        }

    override suspend fun getServerSettingsInfo() : Result<ServerSettings> =
        cancellableRunCatching {
            val response = settingsInfoAPI.getServerSettingsInfo()
            if (!response.success) throw Exception(response.message)

            response.newState?.toDomain() ?: throw Exception("Server returned no state")
        }
}