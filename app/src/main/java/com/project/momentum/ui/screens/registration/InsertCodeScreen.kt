package com.project.momentum.ui.screens.registration

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.momentum.R
import com.project.momentum.data.LoginType
import com.project.momentum.data.registration.RegistrationNavEvent
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.screens.friends.UserViewModel

@Composable
fun InsertCodeScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: RegistrationViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                RegistrationNavEvent.NavigateToNextScreen -> onContinueClick()
                else -> onBackClick()
            }
        }
    }
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
            onValueChange = { viewModel.updateUserCode(it) },
            onBackClick = {
                viewModel.previousStep()
                onBackClick()
            },
            onContinueClick = {
                viewModel.nextStep()
            },
            onSubButtonClick = {
                //TODO: ресерч как отправлять код
//            viewModel.onSendCodeAgainClick
            },
            modifier = modifier,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InsertCodeScreenPreview() {
    InsertCodeScreen(
        onBackClick = {},
        onContinueClick = {},
    )
}