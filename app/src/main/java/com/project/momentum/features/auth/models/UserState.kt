package com.project.momentum.features.auth.models

import com.project.momentum.features.auth.models.UserData
import com.project.momentum.features.auth.viewmodel.ErrorLogin

enum class LoginStep {
    LOGIN,
    PASSWORD,
    PASSWORD_RECOVERY,
    VERIFICATION,
    COMPLETED
}

enum class LoginType {
    EMAIL, PHONE, VKID
}

data class LoginState(
    val currentStep: LoginStep = LoginStep.LOGIN,
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val loginType: LoginType = LoginType.EMAIL,
    val errorMessage: ErrorLogin = ErrorLogin.None,
    val canGoNext: Boolean = false,
    val canGoBack: Boolean = true
)