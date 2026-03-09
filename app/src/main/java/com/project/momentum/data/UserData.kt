package com.project.momentum.data

data class UserData(
    val email: String = "",
    val phone: String? = null,
    val password: String = "",
    val verificationCode: String = "",
)
