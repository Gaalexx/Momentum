package com.project.momentum.domain.auth.repository

import com.project.momentum.domain.auth.model.AuthCredentials

interface RegistrationRepository {
    suspend fun checkRegistrationLoginDB(user: AuthCredentials): Boolean
    suspend fun checkAuthorizationLoginDB(user: AuthCredentials): Boolean
    suspend fun sendCode(user: AuthCredentials): Boolean
    suspend fun checkRegistrationUserCode(user: AuthCredentials): Boolean
    suspend fun checkAuthorizationUserCode(user: AuthCredentials): String?
    suspend fun register(user: AuthCredentials): String?
    suspend fun login(user: AuthCredentials): String?
    suspend fun authorizeWithVK(vkAccessToken: String): String?
    suspend fun authorize(): String?
    suspend fun syncPushToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearSession(): Boolean
    suspend fun clearLocalAuthData()
}
