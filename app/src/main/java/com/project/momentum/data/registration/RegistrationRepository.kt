package com.project.momentum.data.registration

import com.project.momentum.data.CheckCodeRequestDTO
import com.project.momentum.data.CheckEmailRequestDTO
import com.project.momentum.data.CheckPhoneNumberRequestDTO
import com.project.momentum.data.LoginResponseDTO
import com.project.momentum.data.LoginType
import com.project.momentum.data.RegisterUserRequestDTO
import com.project.momentum.data.LoginState
import com.project.momentum.data.LoginUserRequestDTO
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegistrationRepository @Inject constructor(
    private val client: RegistrationClient
) {
    suspend fun checkRegistrationLoginDB(user: LoginState): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToRegistrationChecker(CheckEmailRequestDTO(user.userData.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.userData.phone ?: ""))
        }
        return response.isSuccess
    }

    suspend fun sendAuthorizationCode(user: LoginState): Boolean {
        val response = client.sendCodeAuthorization(CheckEmailRequestDTO(user.userData.email))
        return response.isSuccess
    }

    suspend fun checkAuthorizationLoginDB(user: LoginState): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToAuthorizationChecker(CheckEmailRequestDTO(user.userData.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.userData.phone ?: ""))
        }
        return response.isSuccess
    }

    suspend fun checkUserCode(user: LoginState): Boolean {
        val response = client.sendCodeToChecker(
            CheckCodeRequestDTO(
                email = user.userData.email,
                phone = null,
                code = user.userData.verificationCode
            )
        )
        return response.isSuccess
    }

    suspend fun sendUserData(user: LoginState): String? {
        val response = client.sendData(
            RegisterUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password
            )
        )
        return response.jwt
    }

    suspend fun login(user: LoginState): String? {
        val response = client.sendLoginData(
            LoginUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password
            )
        )
        return response.jwt
    }
}