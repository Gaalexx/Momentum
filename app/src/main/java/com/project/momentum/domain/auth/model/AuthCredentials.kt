package com.project.momentum.domain.auth.model

enum class LoginType {
    EMAIL, PHONE, VKID
}

/** User input needed to authenticate; independent of screen state and transport DTOs. */
data class AuthCredentials(
    val loginType: LoginType = LoginType.EMAIL,
    val email: String = "",
    val phone: String? = null,
    val password: String = "",
    val verificationCode: String = "",
)
