package com.project.momentum.features.settings.api

import android.util.Log
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.remote.RegistrationAPI
import com.project.momentum.features.auth.models.dto.CheckCodeLoginRequestDTO
import com.project.momentum.features.auth.models.dto.CheckCodeLoginResponseDTO
import com.project.momentum.features.auth.models.dto.CheckCodeRequestDTO
import com.project.momentum.features.auth.models.dto.CheckEmailRequestDTO
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
import com.project.momentum.features.auth.models.dto.GetJWTDTO
import com.project.momentum.features.auth.models.dto.LoginResponseDTO
import com.project.momentum.features.auth.models.dto.LoginUserRequestDTO
import com.project.momentum.features.settings.models.dto.SettingsBooleanDTO
import com.project.momentum.features.settings.models.dto.SettingsCheckPasswordDTO
import com.project.momentum.network.di.Backend
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
    suspend fun deleteAccount(email: CheckEmailRequestDTO): SettingsBooleanDTO
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

    override suspend fun deleteAccount(email: CheckEmailRequestDTO): SettingsBooleanDTO {
        val response = client.post("delete-account") {
            setBody(email)
        }

        return try {
            response.body<SettingsBooleanDTO>()
        } catch (e: ConnectException) {
            Log.e("tryDeleteAccount", response.toString())
            SettingsBooleanDTO(false, "ConnectException")
        }
    }
}