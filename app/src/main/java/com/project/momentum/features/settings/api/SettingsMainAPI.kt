package com.project.momentum.features.settings.api

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
import com.project.momentum.features.settings.models.dto.SwitchStateDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton

interface ISettingsMainAPI {
    suspend fun changeInAppNotifications(): CheckResponseDTO
    suspend fun changePublicationsEnabled(): CheckResponseDTO
    suspend fun changeReactionsEnabled(): CheckResponseDTO
    suspend fun changeRecommendToContacts(): CheckResponseDTO
    suspend fun changeAllowAddFromAnyone(): CheckResponseDTO
    suspend fun changeConfirmBeforePosting(): CheckResponseDTO

    suspend fun getInAppNotifications(): SwitchStateDTO
    suspend fun getPublicationsEnabled(): SwitchStateDTO
    suspend fun getReactionsEnabled(): SwitchStateDTO
    suspend fun getRecommendToContacts(): SwitchStateDTO
    suspend fun getAllowAddFromAnyone(): SwitchStateDTO
    suspend fun getConfirmBeforePosting(): SwitchStateDTO
}

@Singleton
class SettingsMainAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
): ISettingsMainAPI{
    override suspend fun changeInAppNotifications() : CheckResponseDTO{
        val response: CheckResponseDTO = client.post("change-in-app-notifications") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<CheckResponseDTO>()

        return response
    }
    override suspend fun changePublicationsEnabled() : CheckResponseDTO{
        val response: CheckResponseDTO = client.post("change-publications-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<CheckResponseDTO>()

        return response
    }
    override suspend fun changeReactionsEnabled() : CheckResponseDTO{
        val response: CheckResponseDTO = client.post("change-reactions-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<CheckResponseDTO>()

        return response
    }
    override suspend fun changeRecommendToContacts() : CheckResponseDTO{
        val response: CheckResponseDTO = client.post("change-recommend-to-contacts") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<CheckResponseDTO>()

        return response
    }
    override suspend fun changeAllowAddFromAnyone() : CheckResponseDTO{
        val response: CheckResponseDTO = client.post("change-allow-add-from-anyone") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<CheckResponseDTO>()

        return response
    }
    override suspend fun changeConfirmBeforePosting() : CheckResponseDTO{
        val response: CheckResponseDTO = client.post("change-confirm-before-posting") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun getInAppNotifications() : SwitchStateDTO{
        val response = client.post("get-in-app-notifications") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SwitchStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun getPublicationsEnabled() : SwitchStateDTO{
        val response = client.post("get-publications-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SwitchStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun getReactionsEnabled() : SwitchStateDTO{
        val response = client.post("get-reactions-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SwitchStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun getRecommendToContacts() : SwitchStateDTO{
        val response = client.post("get-recommend-to-contacts") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SwitchStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun getAllowAddFromAnyone() : SwitchStateDTO {
        val response = client.post("get-allow-add-from-anyone") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SwitchStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun getConfirmBeforePosting() : SwitchStateDTO{
        val response = client.post("get-confirm-before-posting") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<SwitchStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
}