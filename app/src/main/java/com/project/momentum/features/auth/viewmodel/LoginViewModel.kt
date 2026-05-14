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

sealed interface ErrorLogin {
    object None: ErrorLogin

    enum class PasswordError: ErrorLogin {
        NOT_MATCH, EMPTY, TOO_SHORT, NO_DIGITS, NO_LOWERCASE_LETTERS, NO_UPPERCASE_LETTERS, INVALID
    }

    enum class LoginError: ErrorLogin {
        ALREADY_EXISTS_IN_DB, NOT_EXISTS, NOT_EXISTS_IN_DB, EMPTY, INVALID_FORMAT
    }

    enum class CodeError: ErrorLogin {
        INVALID, EMPTY
    }

    enum class DeleteError: ErrorLogin {
        NOT_EXISTS
    }

}

open class LoginViewModel : ViewModel() {
    protected var _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    protected val _navigationEvents = MutableSharedFlow<NavEvent>()
    val navigationEvents: SharedFlow<NavEvent> = _navigationEvents.asSharedFlow()

    fun validateCurrentStep(validatePassword: ErrorLogin = isValidPassword()) {
        val isValid: ErrorLogin = when (_state.value.currentStep) {
            LoginStep.LOGIN -> when (_state.value.loginType) {
                LoginType.EMAIL -> isValidEmail()
                else -> isValidPhone()
            }

            LoginStep.PASSWORD_RECOVERY -> when (_state.value.loginType) {
                LoginType.EMAIL -> isValidEmail()
                else -> isValidPhone()
            }

            LoginStep.PASSWORD -> validatePassword
            LoginStep.VERIFICATION -> isValidCode()
            LoginStep.COMPLETED -> ErrorLogin.None
        }

        _state.update {
            it.copy(
                isError = isValid !is ErrorLogin.None,
                errorMessage = isValid,
                canGoNext = isValid !is ErrorLogin.None && !it.isLoading,
                canGoBack = it.currentStep != LoginStep.LOGIN
            )
        }
    }

    fun isValidEmail(): ErrorLogin =
        if (Patterns.EMAIL_ADDRESS.matcher(_state.value.userData.email).matches())
            ErrorLogin.None
        else ErrorLogin.LoginError.INVALID_FORMAT

    fun isValidPhone(): ErrorLogin =
        if (Patterns.PHONE.matcher(_state.value.userData.phone ?: "").matches())
            ErrorLogin.None
        else ErrorLogin.LoginError.INVALID_FORMAT

    open fun isValidPassword(): ErrorLogin {
        val password = _state.value.userData.password

        if (password.isBlank()) {
            return ErrorLogin.PasswordError.EMPTY
        }
        if (password.length < 8) {
            return ErrorLogin.PasswordError.TOO_SHORT
        }
        return when {
            !password.contains(Regex("[0-9]")) -> ErrorLogin.PasswordError.NO_DIGITS
            !password.contains(Regex("[a-z]")) -> ErrorLogin.PasswordError.NO_LOWERCASE_LETTERS
            !password.contains(Regex("[A-Z]")) -> ErrorLogin.PasswordError.NO_UPPERCASE_LETTERS
            else -> ErrorLogin.None
        }
    }

    fun isValidCode(): ErrorLogin {
        return ErrorLogin.None
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