package com.project.momentum.data.auth

import com.project.momentum.data.auth.datastore.AuthStorage
import com.project.momentum.data.auth.keystore.KeystoreManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authAPI: AuthAPI,
    private val authStorage: AuthStorage,
    private val keyStoreManager: KeystoreManager
) {

    suspend fun authorize(): String? {
        val token = authStorage.getEncryptedData() ?: return null
        val decrypted = keyStoreManager.decrypt(token)
        val response = authAPI.tryAuth(decrypted)

        return response.token
    }


}