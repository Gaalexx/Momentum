package com.project.momentum.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.R
import com.project.momentum.ui.assets.TemplateAuthorizationScreen

@Composable
fun InsertCodeScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUsingEmail: Boolean = true,
) {
    TemplateAuthorizationScreen(
        label = R.string.label_create_account,
        title =
            if (isUsingEmail) R.string.insert_code_email
            else R.string.insert_code_phone,
        subButtonText = R.string.button_send_code_again,
        onBackClick = onBackClick,
        onContinueClick = onContinueClick,
        onSubButtonClick = onSendCodeAgainClick,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType =
                if (isUsingEmail) KeyboardType.Email
                else KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun InsertCodeScreenPreview() {
    InsertCodeScreen(
        onBackClick = {},
        onContinueClick = {},
        onSendCodeAgainClick = {}
    )
}