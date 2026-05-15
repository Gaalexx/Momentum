package com.project.momentum.data.remote

import android.util.Log
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.auth.models.dto.GetJWTDTO
import com.project.momentum.features.auth.models.dto.GetJWTPushDTO
import com.project.momentum.features.auth.models.dto.LoginResponseDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.serialization.Serializable
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class LogoutRequestDTO(
    val refreshToken: String
)

@Serializable
data class LogoutResponseDTO(
    val success: Boolean,
    val message: String
)


interface IAuthAPI {
    suspend fun tryAuth(token: String): GetJWTDTO?
    suspend fun trySyncToken(pushToken: String): GetJWTDTO?

    suspend fun tryUnauthorize(refreshToken: String): LogoutResponseDTO
}

@Singleton
class AuthAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
) : IAuthAPI {

    override suspend fun tryAuth(token: String): GetJWTDTO {
        val result = client.post("auth") {
            setBody(GetJWTDTO(token))
        }

        return try {
            result.body<GetJWTDTO>()
        } catch (e: ConnectException) {
            Log.e("tryAuth", result.toString())
            GetJWTDTO(null)
        }
    }

    override suspend fun trySyncToken(pushToken: String): GetJWTDTO {
        val result = client.post("sync-push-token") {
            setBody(GetJWTDTO(pushToken))
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }

        return try {
            result.body<GetJWTDTO>()
        } catch (e: ConnectException) {
            Log.e("sync token", result.toString())
            GetJWTDTO(null)
        }
    }

    override suspend fun tryUnauthorize(refreshToken: String): LogoutResponseDTO {
        val result = client.post("logout") {
            setBody(LogoutRequestDTO(refreshToken))
        }

        return try {
            result.body<LogoutResponseDTO>()
        } catch (e: Exception) {
            LogoutResponseDTO(false, "Error")
        }
    }


}