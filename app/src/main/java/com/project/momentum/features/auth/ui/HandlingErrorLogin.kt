package com.project.momentum.features.auth.ui

import android.util.Log
import androidx.compose.runtime.Composable
import com.project.momentum.features.auth.models.LoginState
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.viewmodel.ErrorLogin

@Composable
fun handlingErrorLogin(state: LoginState): String = // TODO: вынести в ресурсы
    when (state.errorMessage) {
        is ErrorLogin.LoginError ->
            when (state.loginType) {
                LoginType.EMAIL ->
                    when (state.errorMessage) { // TODO: уточнить ошибки ПОЧТЫ
                        ErrorLogin.LoginError.ALREADY_EXISTS_IN_DB -> "Пользователь уже существует"
                        ErrorLogin.LoginError.NOT_EXISTS -> "Пользователь не существует"
                        ErrorLogin.LoginError.NOT_EXISTS_IN_DB -> "Пользователь не существует в базе"
                        ErrorLogin.LoginError.INVALID_FORMAT -> "Неверный формат"
                        ErrorLogin.LoginError.EMPTY -> "Поле пустое"
                    }
                else ->
                    when (state.errorMessage) { // TODO: уточнить ошибки ТЕЛЕФОНА
                        ErrorLogin.LoginError.ALREADY_EXISTS_IN_DB -> "Пользователь уже существует"
                        ErrorLogin.LoginError.NOT_EXISTS -> "Пользователь не существует"
                        ErrorLogin.LoginError.NOT_EXISTS_IN_DB -> "Пользователь не существует в базе"
                        ErrorLogin.LoginError.INVALID_FORMAT -> "Неверный формат"
                        ErrorLogin.LoginError.EMPTY -> "Поле пустое"
                    }
            }
        is ErrorLogin.PasswordError ->
            when(state.errorMessage) {
                ErrorLogin.PasswordError.EMPTY -> "Поле пустое"
                ErrorLogin.PasswordError.TOO_SHORT -> "Пароль слишком короткий"
                ErrorLogin.PasswordError.NO_DIGITS -> "Пароль должен содержать хотя бы одну цифру"
                ErrorLogin.PasswordError.NO_LOWERCASE_LETTERS -> "Пароль должен содержать хотя бы одну строчную букву"
                ErrorLogin.PasswordError.NO_UPPERCASE_LETTERS -> "Пароль должен содержать хотя бы одну заглавную букву"
                ErrorLogin.PasswordError.NOT_MATCH -> "Пароли не совпадают"
            }
        is ErrorLogin.CodeError ->
            when(state.errorMessage) {
                ErrorLogin.CodeError.EMPTY -> "Поле пустое"
                ErrorLogin.CodeError.INVALID -> "Неверный код"
            }
        is ErrorLogin.None -> {
            Log.e("Momentum", "isError = true && errorMessage = ErrorLogin.None, in HandlingErrorLogin")
            ""
        }
    }