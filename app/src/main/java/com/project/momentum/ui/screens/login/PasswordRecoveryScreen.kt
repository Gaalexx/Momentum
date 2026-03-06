package com.project.momentum.ui.screens.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.momentum.R
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.screens.registration.RegistrationViewModel

@Composable
fun PasswordRecoveryScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = viewModel(),
) {
    val uiState by viewModel.state.collectAsState()

    TemplateAuthorizationScreen(
        value = uiState.userData.verificationCode,
        label = R.string.label_password_recovery,
        title =
            if (uiState.isUsingEmail) R.string.insert_email
            else R.string.insert_phone_number,
        subButtonText =
            if (uiState.isUsingEmail) R.string.button_use_phone_number
            else R.string.button_use_email,
        onValueChange = { viewModel.updateUserCode(it) },
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onContinueClick = {
            viewModel.nextStep() // TODO: viewModel.checkRecoveryCode()
            onContinueClick()
        },
        onSubButtonClick = onSendCodeAgainClick,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType =
                if (uiState.isUsingEmail) KeyboardType.Email
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