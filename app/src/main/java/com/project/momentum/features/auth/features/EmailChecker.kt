package com.project.momentum.features.auth.features

import android.util.Log
import com.project.momentum.features.auth.models.dto.EmailResponse
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class EmailChecker @Inject constructor(
    private val client: EmailCheckerAPI,
) {
    suspend fun checkEmail(email: String): Boolean {
        val response = client.checkEmail(email)
        Log.d("EmailChecker", response.toString())
        return response.smtpCheck
    }
}