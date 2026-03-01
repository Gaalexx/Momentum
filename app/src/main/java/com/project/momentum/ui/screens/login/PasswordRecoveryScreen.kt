package com.project.momentum.ui.screens.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.R
import com.project.momentum.ui.assets.TemplateAuthorizationScreen

@Composable
fun PasswordRecoveryScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUsingEmail: Boolean = true,
) {
    TemplateAuthorizationScreen(
        label = R.string.label_password_recovery,
        title =
            if (isUsingEmail) R.string.insert_email
            else R.string.insert_phone_number,
        subButtonText =
            if (isUsingEmail) R.string.button_use_phone_number
            else R.string.button_use_email,
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
fun PasswordRecoveryScreenPreview() {
    PasswordRecoveryScreen(
        onBackClick = {},
        onContinueClick = {},
        onSendCodeAgainClick = {}
    )
}