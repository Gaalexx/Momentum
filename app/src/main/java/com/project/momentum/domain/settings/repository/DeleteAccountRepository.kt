package com.project.momentum.domain.settings.repository

interface DeleteAccountRepository {
    suspend fun checkPassword(password: String): Result<Boolean>
    suspend fun sendCode(): Boolean
    suspend fun checkUserCode(code: String): Result<Boolean>
    suspend fun deleteAccount(): Result<Boolean>
}
