package com.project.momentum.features.settings.repo

import com.project.momentum.features.settings.api.SettingsMainAPI
import com.project.momentum.features.settings.models.dto.SwitchStateDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsMainRepository @Inject constructor(
    private val client: SettingsMainAPI
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




    suspend fun getInAppNotifications() : Boolean?{
        return try {
            client.getInAppNotifications().switchState
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getPublicationsEnabled() : Boolean?{
        return try {
            client.getPublicationsEnabled().switchState
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getReactionsEnabled() : Boolean?{
        return try {
            client.getReactionsEnabled().switchState
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRecommendToContacts() : Boolean?{
        return try {
            client.getRecommendToContacts().switchState
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllowAddFromAnyone() : Boolean? {
        return try {
            client.getAllowAddFromAnyone().switchState
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getConfirmBeforePosting() : Boolean?{
        return try {
            client.getConfirmBeforePosting().switchState
        } catch (e: Exception) {
            null
        }
    }
}