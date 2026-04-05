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
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.AuthorizationViewModel
import com.project.momentum.ui.assets.TemplateAuthorizationScreen

@Composable
fun AuthorizationPasswordRoot(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onPasswordRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AuthorizationViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavEvent.NavigateToNextScreen -> onContinueClick()
                NavEvent.NavigateToNextSubScreen -> onPasswordRecoveryClick()
                else -> onBackClick()
            }
        }
    }

    AuthorizationPasswordScreen(
        uiState = uiState,
        onValueChange = { viewModel.updateUserPassword(it) },
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onContinueClick = {
            viewModel.nextStep()
        },
        onSubButtonClick = {
            // обработать во вьюмодели
            viewModel.onCodeAuthorization()
        }
    )
}

@Composable
fun AuthorizationPasswordScreen(
    uiState: LoginState,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSubButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TemplateAuthorizationScreen(
        value = uiState.userData.password,
        label = R.string.label_authorization,
        title = R.string.insert_password,
        subButtonText = R.string.button_use_code,
        onValueChange = onValueChange,
        onBackClick = onBackClick,
        onContinueClick = onContinueClick,
        onSubButtonClick = onSubButtonClick,
        modifier = modifier,
        placeholder = stringResource(R.string.placeholder_password),
        isError = uiState.isError,
        errorText = uiState.errorMessage,
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
        uiState = LoginState(),
        onValueChange = {},
        onBackClick = {},
        onContinueClick = {},
        onSubButtonClick = {}
    )
}