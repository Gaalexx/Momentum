package com.project.momentum.features.settings.repo

import com.project.momentum.data.auth.deviceinfo.DeviceInfo
import com.project.momentum.features.auth.models.dto.CheckCodeLoginRequestDTO
import com.project.momentum.features.auth.models.dto.CheckCodeRequestDTO
import com.project.momentum.features.auth.models.dto.CheckEmailRequestDTO
import com.project.momentum.features.auth.models.dto.LoginUserRequestDTO
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.features.settings.api.DeleteAccountAPI
import com.project.momentum.features.settings.models.dto.SettingsCheckPasswordDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAccountRepository @Inject constructor(
    private val client: DeleteAccountAPI,
    private val deviceInfo: DeviceInfo
) {
    suspend fun checkPassword(user: DeleteAccountState): Result<Boolean> =
        runCatching {
            val response = client.checkPassword(
                SettingsCheckPasswordDTO(
                    password = user.userData.password
                )
            )
            if (!response.success) throw Exception(response.message)
            true
        }

    suspend fun sendCode(): Boolean {
        val response = client.sendCode()
        if (!response.success) throw Exception(response.message)
        return response.success
    }

    suspend fun checkUserCode(code: String): Result<Boolean> =
        runCatching {
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


    suspend fun deleteAccount(): Result<Boolean> =
        runCatching {
            val response = client.deleteAccount()
            if (!response.success) throw Exception(response.message)
            true
        }

}