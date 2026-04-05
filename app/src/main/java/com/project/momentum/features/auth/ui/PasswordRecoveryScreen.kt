package com.project.momentum.features.auth.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
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
import com.project.momentum.features.auth.viewmodel.AuthorizationViewModel
import com.project.momentum.ui.assets.TemplateAuthorizationScreen

@Composable
fun PasswordRecoveryRoot(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
//    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AuthorizationViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavEvent.NavigateToNextScreen -> onContinueClick()
                else -> onBackClick()
            }
        }
    }

    PasswordRecoveryScreen(
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
            viewModel.switchLoginType()
//            viewModel.onSendCodeAgainClick()
        },
        modifier = modifier
    )
}

@Composable
fun PasswordRecoveryScreen(
    uiState: LoginState,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSubButtonClick: () -> Unit,
//    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    TemplateAuthorizationScreen(
        value = uiState.userData.verificationCode,
        label = R.string.label_authorization,
        title =
            when (uiState.loginType) {
                LoginType.EMAIL -> R.string.insert_code_email
                else -> R.string.insert_code_phone
            },
        subButtonText =
            when (uiState.loginType) {
                LoginType.EMAIL -> R.string.button_use_phone_number
                else -> R.string.button_use_email
            },
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

@Preview(showBackground = true)
@Composable
fun PasswordRecoveryScreenPreview() {
    PasswordRecoveryScreen(
        uiState = LoginState(currentStep = LoginStep.VERIFICATION),
        onValueChange = {},
        onBackClick = {},
        onContinueClick = {},
        onSubButtonClick = {},
//        onSendCodeAgainClick = {}
    )
}