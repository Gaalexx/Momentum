package com.project.momentum.features.settings.models

enum class DeleteAccountStep {
    PASSWORD,
    VERIFICATION,
    DELETE_ACCOUNT_CONFIRMATION,
    DELETE_ACCOUNT,
    COMPLETED
}

data class UserData(
    val passwordFstTextField: String = "",
    val passwordScdTextField: String = "",
    val email: String = "",
    val phone: String? = null,
    val password: String = "",
    val verificationCode: String = "",
)

data class DeleteAccountState (
    val currentStep: DeleteAccountStep = DeleteAccountStep.PASSWORD,
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isStepValid: Boolean = true,
    val canGoNext: Boolean = false,
    val canGoBack: Boolean = false,
    val showConfirmationDialog: Boolean = false
)