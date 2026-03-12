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
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.AuthorizationViewModel
import com.project.momentum.ui.assets.TemplateAuthorizationScreen

@Composable
fun AuthorizationAccountScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
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

    TemplateAuthorizationScreen(
        value = when (uiState.loginType) {
            LoginType.EMAIL -> uiState.userData.email
            else -> uiState.userData.phone ?: ""
        },
        label = R.string.label_authorization,
        title =
            when (uiState.loginType) {
                LoginType.EMAIL -> R.string.insert_email
                else -> R.string.insert_phone_number
            },
        subButtonText =
            when (uiState.loginType) {
                LoginType.EMAIL -> R.string.button_authorization_by_phone_number
                else -> R.string.button_authorization_by_email
            },
        onValueChange = { viewModel.updateUserEmail(it) },
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onContinueClick = {
            viewModel.nextStep()
        },
        onSubButtonClick = {
            viewModel.switchLoginType()
        },
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

@Preview
@Composable
fun AuthorizationAccountScreenPreview() {
    AuthorizationAccountScreen(
        onBackClick = {},
        onContinueClick = {},
    )
}