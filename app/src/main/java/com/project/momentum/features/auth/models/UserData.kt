package com.project.momentum.features.auth.models

data class UserData(
    val email: String = "",
    val phone: String? = null,
    val password: String = "",
    val verificationCode: String = "",
)