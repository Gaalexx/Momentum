package com.project.momentum.data.auth.repository

import android.util.Log
import com.project.momentum.data.auth.remote.AuthAPI
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.auth.datastore.AuthStorageImpl
import com.project.momentum.data.auth.deviceinfo.DeviceInfo
import com.project.momentum.data.auth.keystore.KeystoreManager
import com.project.momentum.data.auth.pushtoken.FirebasePushTokenProvider
import com.project.momentum.data.auth.remote.RegistrationAPI
import com.project.momentum.domain.auth.model.AuthCredentials
import com.project.momentum.domain.auth.repository.RegistrationRepository
import com.project.momentum.domain.auth.model.LoginType
import com.project.momentum.data.auth.remote.AuthorizeVKRequestDTO
import com.project.momentum.data.auth.remote.CheckCodeLoginRequestDTO
import com.project.momentum.data.auth.remote.CheckCodeRequestDTO
import com.project.momentum.data.auth.remote.CheckEmailRequestDTO
import com.project.momentum.data.auth.remote.CheckPhoneNumberRequestDTO
import com.project.momentum.data.auth.remote.LoginUserRequestDTO
import com.project.momentum.data.auth.remote.RegisterUserRequestDTO
import kotlinx.coroutines.CancellationException
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepositoryImpl @Inject constructor(
    private val client: RegistrationAPI,
    private val authAPI: AuthAPI,
    private val authStorage: AuthStorageImpl,
    private val keyStoreManager: KeystoreManager,
    private val sessionManager: SessionManager,
    private val deviceInfo: DeviceInfo,
    private val pushTokenProvider: FirebasePushTokenProvider
) : RegistrationRepository {
    override suspend fun checkRegistrationLoginDB(user: AuthCredentials): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToRegistrationChecker(CheckEmailRequestDTO(user.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.phone ?: ""))
        }
        return response.isSuccess
    }

    override suspend fun sendCode(user: AuthCredentials): Boolean {
        val response = client.sendCode(CheckEmailRequestDTO(user.email))
        return response.isSuccess
    }

    override suspend fun checkAuthorizationLoginDB(user: AuthCredentials): Boolean {
        val response = when (user.loginType) {
            LoginType.EMAIL -> client.sendEmailToAuthorizationChecker(CheckEmailRequestDTO(user.email))
            else -> client.sendPhoneToChecker(CheckPhoneNumberRequestDTO(user.phone ?: ""))
        }
        return response.isSuccess
    }

    override suspend fun checkRegistrationUserCode(user: AuthCredentials): Boolean {
        val response = client.sendCodeToChecker(
            CheckCodeRequestDTO(
                email = user.email,
                phone = null,
                code = user.verificationCode
            )
        )
        return response.isSuccess
    }

    override suspend fun checkAuthorizationUserCode(user: AuthCredentials): String? {
        val response = client.sendCodeToCheckerForAuthorization(
            CheckCodeLoginRequestDTO(
                email = user.email,
                phone = user.phone,
                code = user.verificationCode,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.token
    }

    override suspend fun register(user: AuthCredentials): String? {
        val response = client.register(
            RegisterUserRequestDTO(
                email = user.email,
                phone = user.phone,
                password = user.password,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    override suspend fun login(user: AuthCredentials): String? {
        val response = client.sendLoginData(
            LoginUserRequestDTO(
                email = user.email,
                phone = user.phone,
                password = user.password,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    override suspend fun authorizeWithVK(vkAccessToken: String): String? {
        val response = client.authorizeWithVK(
            AuthorizeVKRequestDTO(
                vkAccessToken = vkAccessToken,
                deviceInfo = deviceInfo.getPhoneInfo()
            )
        )
        return response.jwt
    }

    override suspend fun authorize(): String? {
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


    override suspend fun syncPushToken(): String? {
        val token = pushTokenProvider.getToken()

        return if (token != null) {
            authAPI.trySyncToken(token).token
        } else {
            null
        }
    }

    override suspend fun saveToken(token: String) {
        val encrypted = keyStoreManager.encrypt(token)
        authStorage.saveEncryptedData(encrypted)
    }

    override suspend fun clearSession(): Boolean {
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

    override suspend fun clearLocalAuthData() {
        try {
            authStorage.clear()
            keyStoreManager.clearKey()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            Log.e("RegistrationRepository", "Error clearing auth data: ${e.message}")
        }
    }
}
