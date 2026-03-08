package com.project.momentum.data

data class UserData(
    val email: String = "email",
    val phone: String? = null,
    val password: String = "password",
    val verificationCode: String = "code",
)
