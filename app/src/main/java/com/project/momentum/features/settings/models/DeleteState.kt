package com.project.momentum.features.settings.models

import com.project.momentum.features.auth.viewmodel.ErrorLogin

enum class DeleteAccountStep {
    PASSWORD,
    PASSWORD_RECOVERY,
    VERIFICATION,
    DELETE_ACCOUNT_CONFIRMATION,
    DELETE_ACCOUNT,
    COMPLETED
}

data class UserData(
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
    val errorMessage: ErrorLogin = ErrorLogin.None,
    val canGoNext: Boolean = false,
    val canGoBack: Boolean = true,
    val showConfirmationDialog: Boolean = false
)