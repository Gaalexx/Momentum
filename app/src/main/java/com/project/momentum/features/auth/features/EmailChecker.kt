package com.project.momentum.features.auth.features

import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException

@Singleton
class EmailChecker {
    suspend fun canReceiveEmail(email: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val domain = email.substringAfter("@")
            domainExists(domain)
        } catch (e: Exception) {
            false
        }
    }

    private fun domainExists(domain: String): Boolean {
        return try {
            InetAddress.getAllByName(domain).isNotEmpty()
        } catch (e: UnknownHostException) {
            false
        }
    }
}