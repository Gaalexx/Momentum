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
fun PasswordRecoveryScreen(
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
        modifier = modifier,
        isError = uiState.isError,
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
        onBackClick = {},
        onContinueClick = {},
//        onSendCodeAgainClick = {}
    )
}