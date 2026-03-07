package com.project.momentum.ui.screens.registration

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
import com.project.momentum.data.LoginType
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.screens.friends.UserViewModel

@Composable
fun InsertCodeScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = viewModel(),
) {
    val uiState by viewModel.state.collectAsState()

    TemplateAuthorizationScreen(
        value = uiState.userData.verificationCode,
        label = R.string.label_create_account,
        title =
            when (uiState.loginType) {
                LoginType.EMAIL ->R.string.insert_code_email
                else -> R.string.insert_code_phone
            },
        subButtonText = R.string.button_send_code_again,
        onValueChange = { viewModel.updateUserCode(it) },
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onContinueClick = {
            viewModel.nextStep()
            onContinueClick()
        },
        onSubButtonClick = onSendCodeAgainClick,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType =
                when (uiState.loginType) {
                    LoginType.EMAIL -> KeyboardType.Email
                    else -> KeyboardType.Phone
                },
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