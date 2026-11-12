package com.project.momentum.domain.auth.repository

interface EmailChecker {
    suspend fun checkEmail(email: String): Boolean
}
