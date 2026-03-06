package com.project.momentum.data

enum class RegistrationStep {
    EMAIL,
    PHONE,
    PASSWORD,
    VERIFICATION,
    COMPLETED
}

data class RegistrationState (
    val currentStep: RegistrationStep = RegistrationStep.EMAIL,
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isUsingEmail: Boolean = true,
    val errorMessage: String? = null,
    val isStepValid: Boolean = false,
    val canGoNext: Boolean = false,
    val canGoBack: Boolean = false
)