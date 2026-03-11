package com.project.momentum.data.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _jwt = MutableStateFlow<String?>(null)
    val jwt: StateFlow<String?> = _jwt

    fun setToken(token: String?) {
        _jwt.value = token
    }

    fun getToken(): String? = _jwt.value

    fun clear() {
        _jwt.value = null
    }

}