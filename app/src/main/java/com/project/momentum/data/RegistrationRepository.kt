package com.project.momentum.data

import android.util.Log
import com.project.momentum.data.remote.AuthAPI
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.auth.datastore.AuthStorageImpl
import com.project.momentum.data.auth.deviceinfo.DeviceInfo
import com.project.momentum.data.auth.keystore.KeystoreManager
import com.project.momentum.data.auth.pushtoken.FirebasePushTokenProvider
import com.project.momentum.data.remote.RegistrationAPI
import com.project.momentum.features.auth.models.LoginState
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.models.dto.AuthorizeVKRequestDTO
import com.project.momentum.features.auth.models.dto.CheckCodeLoginRequestDTO
import com.project.momentum.features.auth.models.dto.CheckCodeRequestDTO
import com.project.momentum.features.auth.models.dto.CheckEmailRequestDTO
import com.project.momentum.features.auth.models.dto.CheckPhoneNumberRequestDTO
import com.project.momentum.features.auth.models.dto.LoginUserRequestDTO
import com.project.momentum.features.auth.models.dto.RegisterUserRequestDTO
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepository @Inject constructor(
    private val client: RegistrationAPI,
    private val authAPI: AuthAPI,
    private val authStorage: AuthStorageImpl,
    private val keyStoreManager: KeystoreManager,
    private val sessionManager: SessionManager,
    private val deviceInfo: DeviceInfo,
    private val pushTokenProvider: FirebasePushTokenProvider
) {
    suspend fun checkRegistrationLoginDB(user: LoginState): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToRegistrationChecker(CheckEmailRequestDTO(user.userData.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.userData.phone ?: ""))
        }
        return response.isSuccess
    }

    suspend fun sendCode(user: LoginState): Boolean {
        val response = client.sendCode(CheckEmailRequestDTO(user.userData.email))
        return response.isSuccess
    }

    suspend fun checkAuthorizationLoginDB(user: LoginState): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToAuthorizationChecker(CheckEmailRequestDTO(user.userData.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.userData.phone ?: ""))
        }
        return response.isSuccess
    }

    suspend fun checkRegistrationUserCode(user: LoginState): Boolean {
        val response = client.sendCodeToChecker(
            CheckCodeRequestDTO(
                email = user.userData.email,
                phone = null,
                code = user.userData.verificationCode
            )
        )
        return response.isSuccess
    }

    suspend fun checkAuthorizationUserCode(user: LoginState): String? {
        val response = client.sendCodeToCheckerForAuthorization(
            CheckCodeLoginRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                code = user.userData.verificationCode,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.token
    }

    suspend fun register(user: LoginState): String? {
        val response = client.register(
            RegisterUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    suspend fun login(user: LoginState): String? {
        val response = client.sendLoginData(
            LoginUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    suspend fun authorizeWithVK(vkAccessToken: String): String? {
        val response = client.authorizeWithVK(
            AuthorizeVKRequestDTO(
                vkAccessToken = vkAccessToken,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    suspend fun authorize(): String? {
        val token = authStorage.getEncryptedData() ?: return null
        val decrypted = try {
            keyStoreManager.decrypt(token)
        } catch (e: GeneralSecurityException) {
            authStorage.clear()
            keyStoreManager.clearKey()
            return null
        }
        val response = authAPI.tryAuth(decrypted)

        if (response.token != null) {
            sessionManager.setToken(response.token)
        }
        return response.token
    }


    suspend fun syncPushToken(): String? {
        val token = pushTokenProvider.getToken()

        return if (token != null) {
            authAPI.trySyncToken(token).token
        } else {
            null
        }
    }

    suspend fun saveToken(token: String) {
        val encrypted = keyStoreManager.encrypt(token)
        authStorage.saveEncryptedData(encrypted)
    }

    suspend fun clearSession(): Boolean {
        val token = authStorage.getEncryptedData() ?: return false
        val decrypted = try {
            keyStoreManager.decrypt(token)
        } catch (e: GeneralSecurityException) {
            authStorage.clear()
            keyStoreManager.clearKey()
            return false
        }

        val res = authAPI.tryUnauthorize(decrypted)
        if (res.success) {
            sessionManager.clear()
            keyStoreManager.clearKey()
            return true
        } else {
            return false
        }
    }

    suspend fun clearLocalAuthData() {
        try {
            authStorage.clear()
            keyStoreManager.clearKey()
        } catch (e: Exception) {
            Log.e("RegistrationRepository", "Error clearing auth data: ${e.message}")
        }
    }
}
