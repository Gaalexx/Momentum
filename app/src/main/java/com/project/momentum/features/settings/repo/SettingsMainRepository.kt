package com.project.momentum.features.settings.repo

import com.project.momentum.features.settings.api.GetSettingsInfoAPI
import com.project.momentum.features.settings.api.SettingsMainAPI
import com.project.momentum.features.settings.models.dto.SettingsStateDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsMainRepository @Inject constructor(
    private val client: SettingsMainAPI,
    private val settingsInfoAPI: GetSettingsInfoAPI
) {
    suspend fun changeInAppNotifications() : Boolean{
        val response = client.changeInAppNotifications()
        return response.isSuccess
    }

    suspend fun changePublicationsEnabled() : Boolean{
        val response = client.changePublicationsEnabled()
        return response.isSuccess
    }

    suspend fun changeReactionsEnabled() : Boolean{
        val response = client.changeReactionsEnabled()
        return response.isSuccess
    }

    suspend fun changeRecommendToContacts() : Boolean{
        val response = client.changeRecommendToContacts()
        return response.isSuccess
    }

    suspend fun changeAllowAddFromAnyone() : Boolean{
        val response = client.changeAllowAddFromAnyone()
        return response.isSuccess
    }

    suspend fun changeConfirmBeforePosting() : Boolean{
        val response = client.changeConfirmBeforePosting()
        return response.isSuccess
    }

    suspend fun getSettingsInfo() : SettingsStateDTO?{
        return try {
            settingsInfoAPI.getSettingsInfo()
        } catch (e: Exception) {
            null
        }
    }
}