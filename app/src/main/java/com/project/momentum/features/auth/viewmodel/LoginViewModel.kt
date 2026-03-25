package com.project.momentum.features.auth.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.project.momentum.features.auth.models.LoginState
import com.project.momentum.features.auth.models.LoginStep
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.features.EmailChecker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

open class LoginViewModel : ViewModel() {
    protected var _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    protected val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents: SharedFlow<NavEvent> = _navigationEvents.asSharedFlow()

    fun validateCurrentStep() {
        val isValid = when (_state.value.currentStep) {
            LoginStep.LOGIN -> when (_state.value.loginType) {
                LoginType.EMAIL -> isValidEmail()
                else -> isValidPhone()
            }

            LoginStep.PASSWORD_RECOVERY -> when (_state.value.loginType) {
                LoginType.EMAIL -> isValidEmail()
                else -> isValidPhone()
            }

            LoginStep.PASSWORD -> isValidPassword() == PasswordState.VALID
            LoginStep.VERIFICATION -> isValidCode()
            LoginStep.COMPLETED -> true
        }

        _state.update {
            it.copy(
                isStepValid = isValid,
                isError = !isValid,
                canGoNext = isValid && !it.isLoading,
                canGoBack = it.currentStep != LoginStep.LOGIN
            )
        }
    }

    fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(_state.value.userData.email).matches()
    }

    fun isValidPhone(): Boolean {
        return Patterns.PHONE.matcher(_state.value.userData.phone ?: "").matches()
    }

    enum class PasswordState {
        VALID, NOT_MATCH, EMPTY, TOO_SHORT, NO_DIGITS, NO_LOWERCASE_LETTERS, NO_UPPERCASE_LETTERS
    }

    open fun isValidPassword(): PasswordState {
        val password = _state.value.userData.password

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
        return true
    }

    fun updateUserEmail(email: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(email = email)
            )
        }
    }

    fun updateUserPhone(phone: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(phone = phone)
            )
        }
    }

    fun updateUserPassword(password: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(password = password)
            )
        }
    }

    fun updateUserCode(code: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(verificationCode = code)
            )
        }
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

    open fun nextStep() {}

    open fun previousStep() {}
}