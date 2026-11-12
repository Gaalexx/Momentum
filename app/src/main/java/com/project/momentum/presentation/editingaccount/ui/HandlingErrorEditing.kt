package com.project.momentum.presentation.editingaccount.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.project.momentum.R
import com.project.momentum.presentation.editingaccount.viewmodel.EditAccountState
import com.project.momentum.presentation.editingaccount.viewmodel.ErrorType

enum class EditingFieldType {
    USERNAME, EMAIL, PHONE
}

@Composable
fun handlingErrorEdit(state: EditAccountState, field: EditingFieldType): String? =
    if (state is EditAccountState.Error) {
        when(field) {
            EditingFieldType.USERNAME -> when (state.getErrorForUsername()) {
                ErrorType.ALREADY_EXIST -> stringResource(R.string.error_text_username_already_exists_in_db)
                ErrorType.WRONG_FORMAT -> stringResource(R.string.error_text_username_wrong_format)
                ErrorType.NOT_EXIST -> stringResource(R.string.error_text_username_wrong_format)
                else -> null
            }
            EditingFieldType.EMAIL -> when (state.getErrorForEmail()) {
                ErrorType.ALREADY_EXIST -> stringResource(R.string.error_text_email_already_exists_in_db)
                ErrorType.WRONG_FORMAT -> stringResource(R.string.error_text_email_not_exists)
                ErrorType.NOT_EXIST -> stringResource(R.string.error_text_email_not_exists)
                else -> null
            }
            EditingFieldType.PHONE -> when (state.getErrorForPhone()) {
                ErrorType.ALREADY_EXIST -> stringResource(R.string.error_text_phone_already_exists_in_db)
                ErrorType.WRONG_FORMAT -> stringResource(R.string.error_text_phone_not_exists)
                ErrorType.NOT_EXIST -> stringResource(R.string.error_text_phone_not_exists)
                else -> null
            }
        }
    } else {
        null
    }