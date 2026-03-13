package com.project.momentum.ui.screens.settings

import com.project.momentum.data.CheckCodeRequestDTO
import com.project.momentum.data.CheckEmailRequestDTO
import com.project.momentum.data.CheckPhoneNumberRequestDTO
import com.project.momentum.data.CheckResponseDTO
import com.project.momentum.data.LoginResponseDTO
import com.project.momentum.data.LoginType
import com.project.momentum.data.RegisterUserRequestDTO
import com.project.momentum.data.DeleteAccountState
import com.project.momentum.data.LoginState
import com.project.momentum.data.LoginUserRequestDTO
import com.project.momentum.data.registration.RegistrationClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAccountRepository @Inject constructor(
    private val client: DeleteAccountClient
) {
    suspend fun login(user: DeleteAccountState): String? {
        val response = client.sendLoginData(
            LoginUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password
            )
        )
        return response.jwt
    }
    suspend fun sendAuthorizationCode(user: DeleteAccountState): Boolean {
        val response = client.sendCodeAuthorization(CheckEmailRequestDTO(user.userData.email))
        return response.isSuccess
    }

    suspend fun checkUserCode(user: DeleteAccountState): Boolean {
        val response = client.sendCodeToChecker(
            CheckCodeRequestDTO(
                email = user.userData.email,
                phone = null,
                code = user.userData.verificationCode
            )
        )
        return response.isSuccess
    }

    suspend fun deleteAccount(user: DeleteAccountState): Boolean {
        val response = client.deleteAccount(
            CheckEmailRequestDTO(
                email = user.userData.email
            )
        )
        return response.isSuccess
    }
}