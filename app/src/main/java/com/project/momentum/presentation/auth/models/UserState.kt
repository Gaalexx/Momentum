package com.project.momentum.presentation.auth.models

import com.project.momentum.domain.auth.model.LoginType
import com.project.momentum.presentation.auth.viewmodel.ErrorLogin

enum class LoginStep {
    LOGIN,
    PASSWORD,
    PASSWORD_RECOVERY,
    VERIFICATION,
    COMPLETED
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