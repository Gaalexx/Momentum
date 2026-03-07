package com.project.momentum.ui.screens.registration

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.data.LoginType
import com.project.momentum.data.RegistrationState
import com.project.momentum.data.RegistrationStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel: ViewModel() {
    private var _state = MutableStateFlow(RegistrationState())

    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private val emailChecker = EmailChecker()

    var passwordRepetition by mutableStateOf("")
        private set


    fun validateCurrentStep() {
        val isValid = when(_state.value.currentStep) {
            RegistrationStep.EMAIL -> isValidEmail()
            RegistrationStep.PHONE -> isValidPhone()
            RegistrationStep.PASSWORD -> isValidPassword() == PasswordState.VALID
            RegistrationStep.VERIFICATION -> isValidCode()
            RegistrationStep.COMPLETED -> true
        }

        _state.update {
            it.copy(
                isStepValid = isValid,
                canGoNext = isValid && !it.isLoading,
                canGoBack = it.currentStep != RegistrationStep.EMAIL
            )
        }
    }

    fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(_state.value.userData.email).matches()
    }

    fun isValidPhone(): Boolean {
        //TODO
        return true
    }

    enum class PasswordState {
        VALID, NOT_MATCH, EMPTY, TOO_SHORT, NO_DIGITS, NO_LOWERCASE_LETTERS, NO_UPPERCASE_LETTERS
    }

    fun isValidPassword(): PasswordState {
        val password = _state.value.userData.password

        if (password != passwordRepetition) {
            return PasswordState.NOT_MATCH
        }
        if (password.isBlank()) {
            return PasswordState.EMPTY
        }
        if (password.length < 8) {
            return PasswordState.TOO_SHORT
        }
        return when {
            !password.contains(Regex("[0-9]")) -> PasswordState.NO_DIGITS
            !password.contains(Regex("[a-z]")) -> PasswordState.NO_LOWERCASE_LETTERS
            !password.contains(Regex("[A-Z]")) -> PasswordState.NO_UPPERCASE_LETTERS
            else -> PasswordState.VALID
        }
    }

    fun isValidCode(): Boolean {
        //TODO: ресерч (наверняка через репозиторий)
        return true
    }
    fun updateUserEmail(email: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(email = email)
            )
        }
    }

    fun updateUserPhone(phone: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(phone = phone)
            )
        }
    }

    fun updateUserPassword(password: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(password = password)
            )
        }
    }

    fun updateUserCode(code: String) {
        _state.update { currentState ->
            currentState.copy (
                userData = currentState.userData.copy(verificationCode = code)
            )
        }
    }

    fun updateUserPasswordRepetition(password: String) {
        passwordRepetition = password
    }

    fun switchLoginType() {
        _state.update {
            it.copy(
                loginType = when (it.loginType) {
                    LoginType.EMAIL -> LoginType.PHONE
                    else -> LoginType.EMAIL
                }
            )
        }
    }

    fun nextStep() {
        if (!_state.value.isStepValid) return

        when (_state.value.currentStep) {
            RegistrationStep.EMAIL -> {
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    //TODO: доработать через репозиторий
                    val exists = emailChecker.canReceiveEmail(_state.value.userData.email)

                    if (exists) {
                        _state.update {
                            it.copy(
                                currentStep = RegistrationStep.VERIFICATION,
                                isError = false,
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isError = true,
                                isLoading = false,
                                errorMessage = "Email не найден"
                            )
                        }
                    }
                }
            }
            RegistrationStep.PHONE -> {
                //TODO: проверка телефона на валидность
                _state.update {
                    it.copy(
                        currentStep = RegistrationStep.VERIFICATION
                    )
                }
            }
            RegistrationStep.VERIFICATION -> {
                _state.update {
                    it.copy(
                        currentStep = RegistrationStep.PASSWORD
                    )
                }
            }
            RegistrationStep.PASSWORD -> {
                _state.update {
                    it.copy(
                        currentStep = RegistrationStep.COMPLETED
                    )
                }
            }
            else -> {}
        }
    }

    fun previousStep() {
        if (!_state.value.canGoBack) return

        _state.update {
            it.copy(
                currentStep = when (it.currentStep) {
                    RegistrationStep.PASSWORD ->
                        when (it.loginType) {
                            LoginType.EMAIL -> RegistrationStep.EMAIL
                            else -> RegistrationStep.PHONE
                        }
                    RegistrationStep.VERIFICATION ->
                        when (it.loginType) {
                            LoginType.EMAIL -> RegistrationStep.EMAIL
                            else -> RegistrationStep.PHONE
                        }
                    else -> it.currentStep
                }
            )
        }
        validateCurrentStep()
    }
}