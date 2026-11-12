package com.project.momentum.data.settings.remote

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.settings.remote.ServerSettingsStateDTO
import com.project.momentum.data.settings.remote.SettingsActionDTO
import com.project.momentum.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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

    suspend fun getServerSettingsInfo() : SettingsActionDTO {
        val response = client.get("settings/get-settings-info") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }
        return response.body<SettingsActionDTO>()
    }
}