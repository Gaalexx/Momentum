package com.project.momentum.ui.screens.settings

import com.project.momentum.data.CheckCodeRequestDTO
import com.project.momentum.data.CheckEmailRequestDTO
import com.project.momentum.data.CheckPhoneNumberRequestDTO
import com.project.momentum.data.CheckResponseDTO
import com.project.momentum.data.LoginResponseDTO
import com.project.momentum.data.LoginUserRequestDTO
import com.project.momentum.data.RegisterUserRequestDTO
import com.project.momentum.data.registration.RegistrationClient
import com.project.momentum.data.s3.PresignedURLDTO
import com.project.momentum.di.Backend
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
    private val regAPI: RegistrationClient
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