package com.project.momentum.data.remote

import android.util.Log
import com.project.momentum.features.auth.models.dto.AuthorizeVKRequestDTO
import com.project.momentum.features.auth.models.dto.CheckCodeLoginRequestDTO
import com.project.momentum.features.auth.models.dto.CheckCodeLoginResponseDTO
import com.project.momentum.features.auth.models.dto.CheckCodeRequestDTO
import com.project.momentum.features.auth.models.dto.CheckEmailRequestDTO
import com.project.momentum.features.auth.models.dto.CheckPhoneNumberRequestDTO
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
import com.project.momentum.features.auth.models.dto.LoginResponseDTO
import com.project.momentum.features.auth.models.dto.LoginUserRequestDTO
import com.project.momentum.features.auth.models.dto.RegisterUserRequestDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Singleton

interface IRegistrationLoginClient {
    suspend fun sendEmailToAuthorizationChecker(email: CheckEmailRequestDTO): CheckResponseDTO

    suspend fun sendEmailToRegistrationChecker(email: CheckEmailRequestDTO): CheckResponseDTO

    suspend fun sendPhoneToChecker(phone: CheckPhoneNumberRequestDTO): CheckResponseDTO

    suspend fun register(userData: RegisterUserRequestDTO): LoginResponseDTO

    suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): CheckResponseDTO

    suspend fun sendCodeToCheckerForAuthorization(code: CheckCodeLoginRequestDTO): CheckCodeLoginResponseDTO

    suspend fun sendLoginData(userData: LoginUserRequestDTO): LoginResponseDTO

    suspend fun sendCode(email: CheckEmailRequestDTO): CheckResponseDTO

    suspend fun authorizeWithVK(dto: AuthorizeVKRequestDTO): LoginResponseDTO
}

@Singleton
class RegistrationAPI @Inject constructor(
    @Backend private val client: HttpClient
) : IRegistrationLoginClient {

    override suspend fun sendEmailToRegistrationChecker(email: CheckEmailRequestDTO): CheckResponseDTO {
        val response = client.post("check-email") {
            setBody(email)
        }

        if (response.status.value in 200..299) {
            return response.body<CheckResponseDTO>()
        } else {
            Log.e("RegistrationAPI", "Server error: ${response.body<String>()}")
            throw Exception("Server error: ${response.status.value}")
        }
    }

    override suspend fun sendEmailToAuthorizationChecker(email: CheckEmailRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("check-email-login") {
            setBody(email)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun sendPhoneToChecker(phone: CheckPhoneNumberRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("check-telephone") {
            setBody(phone)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun register(userData: RegisterUserRequestDTO): LoginResponseDTO {
        val response: LoginResponseDTO = client.post("register") {
            setBody(userData)
        }.body<LoginResponseDTO>()

        return response
    }

    override suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("check-code") {
            setBody(code)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun sendCodeToCheckerForAuthorization(code: CheckCodeLoginRequestDTO): CheckCodeLoginResponseDTO {
        val response: CheckCodeLoginResponseDTO = client.post("check-login-code") {
            setBody(code)
        }.body<CheckCodeLoginResponseDTO>()

        return response
    }

    override suspend fun sendCode(email: CheckEmailRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("send-code") {
            setBody(email)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun sendLoginData(userData: LoginUserRequestDTO): LoginResponseDTO {
        val response: LoginResponseDTO = client.post("login") {
            setBody(userData)
        }.body<LoginResponseDTO>()

        return response
    }


    override suspend fun authorizeWithVK(dto: AuthorizeVKRequestDTO): LoginResponseDTO {
        val response = client.post("auth/vk") {
            setBody(dto)
        }
        return if (response.status.value in 200..299) {
            response.body<LoginResponseDTO>()
        } else {
            LoginResponseDTO(jwt = null)
        }
    }

}