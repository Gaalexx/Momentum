package com.project.momentum.data.registration

import com.project.momentum.data.CheckCodeRequestDTO
import com.project.momentum.data.CheckEmailRequestDTO
import com.project.momentum.data.CheckPhoneNumberRequestDTO
import com.project.momentum.data.CheckResponseDTO
import com.project.momentum.data.LoginResponseDTO
import com.project.momentum.data.RegisterUserRequestDTO
import com.project.momentum.data.s3.PresignedURLDTO
import com.project.momentum.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Singleton

interface IRegistrationLoginClient {
    suspend fun sendEmailToChecker(email: CheckEmailRequestDTO): CheckResponseDTO

    suspend fun sendPhoneToChecker(phone: CheckPhoneNumberRequestDTO): CheckResponseDTO

    suspend fun sendData(userData: RegisterUserRequestDTO): LoginResponseDTO

    suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): CheckResponseDTO
}

@Singleton
class RegistrationClient @Inject constructor(
    @Backend private val client: HttpClient
): IRegistrationLoginClient {

    override suspend fun sendEmailToChecker(email: CheckEmailRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("/check-email") {
            setBody(email)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun sendPhoneToChecker(phone: CheckPhoneNumberRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("/check-telephone") {
            setBody(phone)
        }.body<CheckResponseDTO>()

        return response
    }

    override suspend fun sendData(userData: RegisterUserRequestDTO): LoginResponseDTO {
        val response: LoginResponseDTO = client.post("/login") {
            setBody(userData)
        }.body<LoginResponseDTO>()

        return response
    }

    override suspend fun sendCodeToChecker(code: CheckCodeRequestDTO): CheckResponseDTO {
        val response: CheckResponseDTO = client.post("/check-code") {
            setBody(code)
        }.body<CheckResponseDTO>()

        return response
    }
}