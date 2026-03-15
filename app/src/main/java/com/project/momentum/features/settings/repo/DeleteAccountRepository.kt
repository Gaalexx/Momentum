package com.project.momentum.features.settings.repo

import com.project.momentum.features.auth.models.dto.CheckCodeRequestDTO
import com.project.momentum.features.auth.models.dto.CheckEmailRequestDTO
import com.project.momentum.features.auth.models.dto.LoginUserRequestDTO
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.features.settings.api.DeleteAccountClient
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
                password = user.userData.password,
                deviceInfo = "Android"              // TODO поменять DTO
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