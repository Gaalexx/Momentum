package com.project.momentum.data

enum class RegistrationStep {
    LOGIN,
    PASSWORD,
    VERIFICATION,
    COMPLETED
}

enum class LoginType {
    EMAIL, PHONE, VKID
}

data class RegistrationState (
    val currentStep: RegistrationStep = RegistrationStep.LOGIN,
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val loginType: LoginType = LoginType.EMAIL,
    val errorMessage: String? = null,
    val isStepValid: Boolean = false,
    val canGoNext: Boolean = false,
    val canGoBack: Boolean = false
)