package com.project.momentum.features.settings.api

import com.project.momentum.data.remote.RegistrationAPI
import com.project.momentum.features.auth.models.dto.CheckCodeRequestDTO
import com.project.momentum.features.auth.models.dto.CheckEmailRequestDTO
import com.project.momentum.features.auth.models.dto.CheckResponseDTO
import com.project.momentum.features.auth.models.dto.LoginResponseDTO
import com.project.momentum.features.auth.models.dto.LoginUserRequestDTO
import com.project.momentum.network.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Singleton

interface IDeleteAccountClient {
    suspend fun sendLoginData(userData: LoginUserRequestDTO): LoginResponseDTO
    suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): CheckResponseDTO

    suspend fun sendCodeAuthorization(email: CheckEmailRequestDTO): CheckResponseDTO

    suspend fun deleteAccount(email: CheckEmailRequestDTO): CheckResponseDTO
}

@Singleton
class DeleteAccountClient @Inject constructor(
    @Backend private val client: HttpClient,
    private val regAPI: RegistrationAPI
): IDeleteAccountClient{
    override suspend fun sendLoginData(userData: LoginUserRequestDTO): LoginResponseDTO = regAPI.sendLoginData(userData)

    override suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("/check-code") {
            setBody(code)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun sendCodeAuthorization(email: CheckEmailRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("/send-code") {
            setBody(email)
    }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun deleteAccount(email: CheckEmailRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("/delete-account") {
            setBody(email)
        }.body<CheckResponseDTO>()

        return response
    }
}