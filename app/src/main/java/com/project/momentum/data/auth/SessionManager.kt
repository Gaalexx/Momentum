package com.project.momentum.data.auth

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _jwt = MutableStateFlow<String?>(null)
    val jwt: StateFlow<String?> = _jwt

    fun getUserId(): String? {
        val token = getToken() ?: return null
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
            JSONObject(payload).getString("sub")
        } catch (e: Exception) {
            Log.e("SessionManager", "Error getting user ID ${e.message ?: ""}", e)
            null
        }
    }

    fun setToken(token: String?) {
        _jwt.value = token
    }

    fun getToken(): String? = _jwt.value

    fun getHeader(): String = "Bearer ${this.getToken()}"

    fun clear() {
        _jwt.value = null
    }

}