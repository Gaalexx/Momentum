package com.project.momentum.features.auth.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.R
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.AuthorizationViewModel
import com.project.momentum.ui.assets.TemplateAuthorizationScreen

@Composable
fun AuthorizationPasswordScreen(
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

    TemplateAuthorizationScreen(
        value = uiState.userData.password,
        label = R.string.label_authorization,
        title = R.string.insert_password,
        subButtonText = R.string.button_use_code,
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
        },
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