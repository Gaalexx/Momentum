package com.project.momentum.features.settings.api

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
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
    suspend fun changeInAppNotifications(newValue: Boolean): CheckResponseDTO
    suspend fun changePublicationsEnabled(newValue: Boolean): CheckResponseDTO
    suspend fun changeReactionsEnabled(newValue: Boolean): CheckResponseDTO
    suspend fun changeRecommendToContacts(newValue: Boolean): CheckResponseDTO
    suspend fun changeAllowAddFromAnyone(newValue: Boolean): CheckResponseDTO
}

@Singleton
class SettingsMainAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
): ISettingsMainAPI{
    override suspend fun changeInAppNotifications(newValue: Boolean) : CheckResponseDTO{
        val response = client.post("change-in-app-notifications") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(newValue)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changePublicationsEnabled(newValue: Boolean) : CheckResponseDTO{
        val response = client.post("change-publications-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(newValue)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeReactionsEnabled(newValue: Boolean) : CheckResponseDTO{
        val response = client.post("change-reactions-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(newValue)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeRecommendToContacts(newValue: Boolean) : CheckResponseDTO{
        val response = client.post("change-recommend-to-contacts") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(newValue)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeAllowAddFromAnyone(newValue: Boolean) : CheckResponseDTO{
        val response = client.post("change-allow-add-from-anyone") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(newValue)
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
}