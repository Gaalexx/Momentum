package com.project.momentum.ui.screens.registration

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class RegistrationViewModel: ViewModel() {
    private var _userData = MutableStateFlow(UserData())

    val userData: StateFlow<UserData> = _userData.asStateFlow()

    private val emailChecker = EmailChecker()
    var isError by mutableStateOf(false)
        private set

    var passwordRepetition by mutableStateOf("")
        private set

    fun updateUserEmail(email: String) {
        _userData.update { currantState ->
            currantState.copy (
                email = email
            )
        }
    }

    fun updateUserPhone(phone: String) {
        _userData.update { currantState ->
            currantState.copy (
                phone = phone
            )
        }
    }

    fun updateUserPassword(password: String) {
        _userData.update { currantState ->
            currantState.copy (
                password = password
            )
        }
    }

    fun updateUserPasswordRepetition(password: String) {
        passwordRepetition = password
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkEmailExists() {
        viewModelScope.launch {
            isError = !emailChecker.canReceiveEmail(_userData.value.email)
        }
    }

    fun isValidPassword(): PasswordState {
        val password = _userData.value.password

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

    enum class PasswordState {
        VALID, NOT_MATCH, EMPTY, TOO_SHORT, NO_DIGITS, NO_LOWERCASE_LETTERS, NO_UPPERCASE_LETTERS
    }
}