package com.project.momentum.data.settings.repository

import com.project.momentum.data.common.cancellableRunCatching

import com.project.momentum.data.auth.remote.CheckCodeRequestDTO
import com.project.momentum.domain.settings.repository.DeleteAccountRepository
import com.project.momentum.data.settings.remote.DeleteAccountAPI
import com.project.momentum.data.settings.remote.SettingsCheckPasswordDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAccountRepositoryImpl @Inject constructor(
    private val client: DeleteAccountAPI
) : DeleteAccountRepository {
    override suspend fun checkPassword(password: String): Result<Boolean> =
        cancellableRunCatching {
            val response = client.checkPassword(
                SettingsCheckPasswordDTO(
                    password = password
                )
            )
            if (!response.success) throw Exception(response.message)
            true
        }

    override suspend fun sendCode(): Boolean {
        val response = client.sendCode()
        if (!response.success) throw Exception(response.message)
        return response.success
    }

    override suspend fun checkUserCode(code: String): Result<Boolean> =
        cancellableRunCatching {
            val response = client.sendCodeToChecker(
                CheckCodeRequestDTO(
                    code = code,
                    email = null,
                    phone = null
                )
            )
            if (!response.success) throw Exception(response.message)
            true
        }


    override suspend fun deleteAccount(): Result<Boolean> =
        cancellableRunCatching {
            val response = client.deleteAccount()
            if (!response.success) throw Exception(response.message)
            true
        }

}
