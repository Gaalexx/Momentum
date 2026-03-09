package com.project.momentum.data

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

data class LoginState (
    val currentStep: LoginStep = LoginStep.LOGIN,
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val loginType: LoginType = LoginType.EMAIL,
    val errorMessage: String? = null,
    val isStepValid: Boolean = true,
    val canGoNext: Boolean = false,
    val canGoBack: Boolean = false
)