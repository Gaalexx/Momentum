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
fun AuthorizationPasswordScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onPasswordRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = viewModel(),
) {
    val uiState by viewModel.state.collectAsState()

    TemplateAuthorizationScreen(
        value = uiState.userData.password,
        label = R.string.label_authorization,
        title = R.string.insert_password,
        subButtonText = R.string.button_password_recovery,
        onValueChange = { viewModel.updateUserPassword(it) },
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onContinueClick = {
            viewModel.nextStep()
            onContinueClick()
        },
        onSubButtonClick = onPasswordRecoveryClick,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}

@Preview
@Composable
fun AuthorizationPasswordScreenPreview() {
    AuthorizationPasswordScreen(
        onBackClick = {},
        onContinueClick = {},
        onPasswordRecoveryClick = {}
    )
}