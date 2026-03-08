package com.project.momentum.data.registration

import com.project.momentum.data.CheckCodeRequestDTO
import com.project.momentum.data.CheckEmailRequestDTO
import com.project.momentum.data.CheckPhoneNumberRequestDTO
import com.project.momentum.data.LoginType
import com.project.momentum.data.RegisterUserRequestDTO
import com.project.momentum.data.RegistrationState
import com.project.momentum.data.UserData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegistrationRepository @Inject constructor(
    private val client: RegistrationClient
) {
    suspend fun checkUserLoginDB(user: RegistrationState): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToChecker(CheckEmailRequestDTO(user.userData.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.userData.phone ?: ""))
        }
        return response.isSuccess
    }

    suspend fun checkUserCode(user: RegistrationState): Boolean {
        val response = client.sendCodeToChecker(CheckCodeRequestDTO(user.userData.verificationCode))
        return response.isSuccess
    }

    suspend fun sendUserData(user: RegistrationState): String {
        val response = client.sendData(
            RegisterUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password
            )
        )
        return response.jwt
    }
}