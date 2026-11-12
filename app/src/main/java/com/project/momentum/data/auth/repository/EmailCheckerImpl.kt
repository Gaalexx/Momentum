package com.project.momentum.data.auth.repository

import android.util.Log
import com.project.momentum.data.auth.remote.EmailCheckerAPI
import com.project.momentum.domain.auth.repository.EmailChecker
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EmailCheckerImpl @Inject constructor(
    private val client: EmailCheckerAPI,
) : EmailChecker {
    override suspend fun checkEmail(email: String): Boolean {
        val response = client.checkEmail(email)
        Log.d("EmailChecker", response.toString())
        return response.status == "VALID"
    }
}