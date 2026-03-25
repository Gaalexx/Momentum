package com.project.momentum.features.settings.api

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSettingsInfoAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
){

    suspend fun getServerSettingsInfo() : ServerSettingsStateDTO {
        val response = client.post("get-settings-info") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<ServerSettingsStateDTO>()
        } else {
            throw Exception() // TODO прописать свои Exception
        }
    }
}