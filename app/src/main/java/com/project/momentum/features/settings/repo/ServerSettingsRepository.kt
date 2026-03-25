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
    suspend fun changeInAppNotifications(newValue: Boolean) : Result<Boolean> =
        runCatching {
            val response = client.changeInAppNotifications(newValue)
            if (!response.isSuccess) throw Exception("Server error")

            newValue
        }

    suspend fun changePublicationsEnabled(newValue: Boolean) : Result<Boolean> =
        runCatching {
            val response = client.changePublicationsEnabled(newValue)
            if (!response.isSuccess) throw Exception("Server error")

            newValue
        }

    suspend fun changeReactionsEnabled(newValue: Boolean) : Result<Boolean> =
        runCatching {
            val response = client.changeReactionsEnabled(newValue)
            if (!response.isSuccess) throw Exception("Server error")

            newValue
        }

    suspend fun changeRecommendToContacts(newValue: Boolean) : Result<Boolean> =
        runCatching {
            val response = client.changeRecommendToContacts(newValue)
            if (!response.isSuccess) throw Exception("Server error")

            newValue
        }

    suspend fun changeAllowAddFromAnyone(newValue: Boolean) : Result<Boolean> =
        runCatching {
            val response = client.changeAllowAddFromAnyone(newValue)
            if (!response.isSuccess) throw Exception("Server error")

            newValue
        }

    suspend fun getServerSettingsInfo() : Result<ServerSettingsStateDTO> =
        runCatching {
            settingsInfoAPI.getServerSettingsInfo()
        }
}