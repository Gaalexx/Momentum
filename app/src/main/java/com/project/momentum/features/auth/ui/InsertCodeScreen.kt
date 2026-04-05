package com.project.momentum.features.auth.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.R
import com.project.momentum.features.auth.models.LoginState
import com.project.momentum.features.auth.models.LoginStep
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.RegistrationViewModel
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.common.LoadingOverlay

@Preview(showBackground = true)
@Composable
fun InsertCodeScreenPreview() {
    InsertCodeScreen(
        uiState = LoginState(currentStep = LoginStep.VERIFICATION),
        onValueChange = {},
        onBackClick = {},
        onContinueClick = {},
        onSubButtonClick = {},
    )
}

@Composable
fun InsertCodeRoot(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: RegistrationViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavEvent.NavigateToNextScreen -> onContinueClick()
                else -> onBackClick()
            }
        }
    }

    InsertCodeScreen(
        uiState = uiState,
        onValueChange = { viewModel.updateUserCode(it) },
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onContinueClick = {
            viewModel.nextStep()
        },
        onSubButtonClick = {
            viewModel.sendCodeAgain()
        },
        modifier = modifier,
    )
}

@Composable
fun InsertCodeScreen(
    uiState: LoginState,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSubButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (uiState.isLoading) {
        LoadingOverlay()
    } else {
        TemplateAuthorizationScreen(
            value = uiState.userData.verificationCode,
            label = R.string.label_create_account,
            title =
                when (uiState.loginType) {
                    LoginType.EMAIL -> R.string.insert_code_email
                    else -> R.string.insert_code_phone
                },
            subButtonText = R.string.button_send_code_again,
            onValueChange = onValueChange,
            onBackClick = onBackClick,
            onContinueClick = onContinueClick,
            onSubButtonClick = onSubButtonClick,
            modifier = modifier,
            placeholder = stringResource(R.string.placeholder_code),
            isError = uiState.isError,
            errorText = uiState.errorMessage,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )
    }
}