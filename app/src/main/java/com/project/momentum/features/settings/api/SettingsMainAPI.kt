package com.project.momentum.features.settings.api

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.settings.models.dto.*
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton

interface ISettingsMainAPI {
    suspend fun changeInAppNotifications(newValue: Boolean): SettingsActionDTO
    suspend fun changePublicationsEnabled(newValue: Boolean): SettingsActionDTO
    suspend fun changeReactionsEnabled(newValue: Boolean): SettingsActionDTO
    suspend fun changeRecommendToContacts(newValue: Boolean): SettingsActionDTO
    suspend fun changeAllowAddFromAnyone(newValue: Boolean): SettingsActionDTO
}

@Singleton
class SettingsMainAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
): ISettingsMainAPI{

    private suspend fun changeSetting(url: String, body: Any) : SettingsActionDTO{
        val response = client.post(url) {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(body)
        }
        return response.body<SettingsActionDTO>()
    }

    override suspend fun changeInAppNotifications(newValue: Boolean) : SettingsActionDTO{
        return changeSetting("settings/change-in-app-notifications", ChangeInAppNotificationsDTO(newValue))
    }
    override suspend fun changePublicationsEnabled(newValue: Boolean): SettingsActionDTO {
        return changeSetting("settings/change-publications-enabled", ChangePublicationsEnabledDTO(newValue))
    }

    override suspend fun changeReactionsEnabled(newValue: Boolean): SettingsActionDTO {
        return changeSetting("settings/change-reactions-enabled", ChangeReactionsEnabledDTO(newValue))
    }

    override suspend fun changeRecommendToContacts(newValue: Boolean): SettingsActionDTO {
        return changeSetting("settings/change-recommend-to-contacts", ChangeRecommendToContactsDTO(newValue))
    }

    override suspend fun changeAllowAddFromAnyone(newValue: Boolean): SettingsActionDTO {
        return changeSetting("settings/change-allow-add-from-anyone", ChangeAllowAddFromAnyoneDTO(newValue))
    }

}