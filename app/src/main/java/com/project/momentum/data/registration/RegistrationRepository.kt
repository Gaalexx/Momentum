package com.project.momentum.data.registration

import com.project.momentum.data.CheckCodeLoginRequestDTO
import com.project.momentum.data.CheckCodeRequestDTO
import com.project.momentum.data.CheckEmailRequestDTO
import com.project.momentum.data.CheckPhoneNumberRequestDTO
import com.project.momentum.data.LoginType
import com.project.momentum.data.RegisterUserRequestDTO
import com.project.momentum.data.LoginState
import com.project.momentum.data.LoginUserRequestDTO
import com.project.momentum.data.auth.AuthAPI
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.data.auth.datastore.AuthStorageImpl
import com.project.momentum.data.auth.keystore.KeystoreManager
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegistrationRepository @Inject constructor(
    private val client: RegistrationClient,
    private val authAPI: AuthAPI,
    private val authStorage: AuthStorageImpl,
    private val keyStoreManager: KeystoreManager,
    private val sessionManager: SessionManager
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
                deviceInfo = "Android" // TODO получать данные об устройстве по-другому
            )
        )
        return response.token
    }

    suspend fun sendUserData(user: LoginState): String? {
        val response = client.sendData(
            RegisterUserRequestDTO(
                email = user.userData.email,
                phone = user.userData.phone,
                password = user.userData.password,
                deviceInfo = "Android" // TODO получать данные об устройстве по-другому
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
                deviceInfo = "Android" // TODO получать данные об устройстве по-другому
            )
        )
        return response.jwt
    }

    suspend fun authorize(): String? {
        val token = authStorage.getEncryptedData() ?: return null
        val decrypted = keyStoreManager.decrypt(token)
        val response = authAPI.tryAuth(decrypted)

        if (response.token != null) {
            sessionManager.setToken(response.token)
        }
        return response.token
    }

    suspend fun saveToken(token: String) {
        val encrypted = keyStoreManager.encrypt(token)
        authStorage.saveEncryptedData(encrypted)
    }
}