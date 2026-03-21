package com.project.momentum.features.settings.api

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
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
}

@Singleton
class SettingsMainAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
): ISettingsMainAPI{
    override suspend fun changeInAppNotifications() : CheckResponseDTO{
        val response = client.post("change-in-app-notifications") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changePublicationsEnabled() : CheckResponseDTO{
        val response = client.post("change-publications-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeReactionsEnabled() : CheckResponseDTO{
        val response = client.post("change-reactions-enabled") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeRecommendToContacts() : CheckResponseDTO{
        val response = client.post("change-recommend-to-contacts") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeAllowAddFromAnyone() : CheckResponseDTO{
        val response = client.post("change-allow-add-from-anyone") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
    override suspend fun changeConfirmBeforePosting() : CheckResponseDTO{
        val response = client.post("change-confirm-before-posting") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<CheckResponseDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
}