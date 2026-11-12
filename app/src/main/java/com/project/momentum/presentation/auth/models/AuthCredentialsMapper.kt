package com.project.momentum.presentation.auth.models

import com.project.momentum.domain.auth.model.AuthCredentials

fun LoginState.toCredentials(): AuthCredentials = AuthCredentials(
    loginType = loginType,
    email = userData.email,
    phone = userData.phone,
    password = userData.password,
    verificationCode = userData.verificationCode,
)
