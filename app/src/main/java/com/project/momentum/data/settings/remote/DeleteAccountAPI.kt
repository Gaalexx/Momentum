package com.project.momentum.data.settings.remote

import android.util.Log
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.auth.remote.RegistrationAPI
import com.project.momentum.data.auth.remote.CheckCodeLoginRequestDTO
import com.project.momentum.data.auth.remote.CheckCodeLoginResponseDTO
import com.project.momentum.data.auth.remote.CheckCodeRequestDTO
import com.project.momentum.data.auth.remote.CheckEmailRequestDTO
import com.project.momentum.data.auth.remote.CheckResponseDTO
import com.project.momentum.data.auth.remote.GetJWTDTO
import com.project.momentum.data.auth.remote.LoginResponseDTO
import com.project.momentum.data.auth.remote.LoginUserRequestDTO
import com.project.momentum.data.settings.remote.SettingsBooleanDTO
import com.project.momentum.data.settings.remote.SettingsCheckPasswordDTO
import com.project.momentum.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

interface IDeleteAccountAPI {
    suspend fun checkPassword(userData: SettingsCheckPasswordDTO): SettingsBooleanDTO
    suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): SettingsBooleanDTO
    suspend fun sendCode(): SettingsBooleanDTO
    suspend fun deleteAccount(): SettingsBooleanDTO
}

@Singleton
class DeleteAccountAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val regAPI: RegistrationAPI,
    private val sessionManager: SessionManager
): IDeleteAccountAPI{

    override suspend fun checkPassword(userData: SettingsCheckPasswordDTO) : SettingsBooleanDTO {
        val response = client.post("settings/check-password") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(userData)
        }

        return try {
            response.body<SettingsBooleanDTO>()
        } catch (e: ConnectException) {
            Log.e("tryCheckPassword", response.toString())
            SettingsBooleanDTO(false, "ConnectException")
        }

    }
    override suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): SettingsBooleanDTO {
        val response = client.post("settings/check-code") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
            setBody(code)
        }

        return try {
            response.body<SettingsBooleanDTO>()
        } catch (e: ConnectException) {
            Log.e("trySendCodeToChecker", response.toString())
            SettingsBooleanDTO(false, "ConnectException")
        }
    }

    override suspend fun sendCode(): SettingsBooleanDTO {
        val response = client.post("settings/send-code") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }

        return try {
            response.body<SettingsBooleanDTO>()
        } catch (e: ConnectException) {
            Log.e("trySendCode", response.toString())
            SettingsBooleanDTO(false, "ConnectException")
        }
    }

    override suspend fun deleteAccount(): SettingsBooleanDTO {
        val response = client.post("settings/delete-account") {
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }

        return try {
            response.body<SettingsBooleanDTO>()
        } catch (e: ConnectException) {
            Log.e("tryDeleteAccount", response.toString())
            SettingsBooleanDTO(false, "ConnectException")
        }
    }
}