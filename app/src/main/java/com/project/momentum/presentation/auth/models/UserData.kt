package com.project.momentum.presentation.auth.models

data class UserData(
    val email: String = "",
    val phone: String? = null,
    val password: String = "",
    val verificationCode: String = "",
)