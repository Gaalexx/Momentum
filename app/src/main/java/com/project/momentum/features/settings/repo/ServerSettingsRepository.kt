package com.project.momentum.features.settings.repo

import com.project.momentum.features.settings.api.GetSettingsInfoAPI
import com.project.momentum.features.settings.api.SettingsMainAPI
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerSettingsRepository @Inject constructor(
    private val client: SettingsMainAPI,
    private val settingsInfoAPI: GetSettingsInfoAPI,
) {
    suspend fun changeInAppNotifications(newValue: Boolean) : Result<ServerSettingsStateDTO> =
        runCatching {
            val response = client.changeInAppNotifications(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState ?: throw Exception("Server returned no state")
        }

    suspend fun changePublicationsEnabled(newValue: Boolean) : Result<ServerSettingsStateDTO> =
        runCatching {
            val response = client.changePublicationsEnabled(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState ?: throw Exception("Server returned no state")
        }

    suspend fun changeReactionsEnabled(newValue: Boolean) : Result<ServerSettingsStateDTO> =
        runCatching {
            val response = client.changeReactionsEnabled(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState ?: throw Exception("Server returned no state")
        }

    suspend fun changeRecommendToContacts(newValue: Boolean) : Result<ServerSettingsStateDTO> =
        runCatching {
            val response = client.changeRecommendToContacts(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState ?: throw Exception("Server returned no state")
        }

    suspend fun changeAllowAddFromAnyone(newValue: Boolean) : Result<ServerSettingsStateDTO> =
        runCatching {
            val response = client.changeAllowAddFromAnyone(newValue)
            if (!response.success) throw Exception(response.message)

            response.newState ?: throw Exception("Server returned no state")
        }

    suspend fun getServerSettingsInfo() : Result<ServerSettingsStateDTO> =
        runCatching {
            settingsInfoAPI.getServerSettingsInfo()
        }
}