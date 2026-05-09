package com.project.momentum.features.settings.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.project.momentum.R
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.viewmodel.ErrorLogin
import com.project.momentum.features.settings.models.DeleteAccountState

@Composable
fun handlingErrorDelete(state: DeleteAccountState): String? =
    when (state.errorMessage) {
        is ErrorLogin.LoginError ->
            when (state.errorMessage) {
                ErrorLogin.LoginError.ALREADY_EXISTS_IN_DB -> stringResource(R.string.error_text_email_already_exists_in_db)
                ErrorLogin.LoginError.NOT_EXISTS -> stringResource(R.string.error_text_email_not_exists)
                ErrorLogin.LoginError.NOT_EXISTS_IN_DB -> stringResource(R.string.error_text_email_not_exists_in_db)
                ErrorLogin.LoginError.INVALID_FORMAT -> stringResource(R.string.error_text_email_not_exists)
                ErrorLogin.LoginError.EMPTY -> stringResource(R.string.error_text_email_empty)
            }
        is ErrorLogin.PasswordError ->
            when(state.errorMessage) {
                ErrorLogin.PasswordError.EMPTY -> stringResource(R.string.error_text_password_empty)
                ErrorLogin.PasswordError.TOO_SHORT -> stringResource(R.string.error_text_password_too_short)
                ErrorLogin.PasswordError.NO_DIGITS -> stringResource(R.string.error_text_password_no_digits)
                ErrorLogin.PasswordError.NO_LOWERCASE_LETTERS -> stringResource(R.string.error_text_password_no_lowercase_letters)
                ErrorLogin.PasswordError.NO_UPPERCASE_LETTERS -> stringResource(R.string.error_text_password_no_uppercase_letters)
                ErrorLogin.PasswordError.NOT_MATCH -> stringResource(R.string.error_text_password_not_match)
                ErrorLogin.PasswordError.INVALID -> stringResource(R.string.error_text_password_invalid)
            }
        is ErrorLogin.CodeError ->
            when(state.errorMessage) {
                ErrorLogin.CodeError.EMPTY -> stringResource(R.string.error_text_code_empty)
                ErrorLogin.CodeError.INVALID -> stringResource(R.string.error_text_code_invalid)
            }
        is ErrorLogin.DeleteError ->
            when(state.errorMessage) {
                ErrorLogin.DeleteError.NOT_EXISTS -> stringResource(R.string.error_text_account_not_exists)
            }
        is ErrorLogin.None -> {
            Log.e("Momentum", "isError = true && errorMessage = ErrorLogin.None, in HandlingErrorDelete")
            null
        }

    }