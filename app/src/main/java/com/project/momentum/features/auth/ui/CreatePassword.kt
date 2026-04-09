package com.project.momentum.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.R
import com.project.momentum.features.auth.models.LoginState
import com.project.momentum.features.auth.models.LoginStep
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.ErrorLogin
import com.project.momentum.features.auth.viewmodel.RegistrationViewModel
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles

@Preview(showBackground = true)
@Composable
fun CreatePasswordScreenPreview() {
    CreatePasswordScreen(
    uiState = LoginState(currentStep = LoginStep.PASSWORD, isError = true, errorMessage = ErrorLogin.PasswordError.NO_DIGITS),
        passwordRepetition = "",
        onValueChangeFirst = {},
        onValueChangeSecond = {},
        onBackClick = {},
        onContinueClick = {}
    )
}

@Composable
fun CreatePasswordRoot(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
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

    CreatePasswordScreen(
        uiState = uiState,
        passwordRepetition = viewModel.passwordRepetition,
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        onValueChangeFirst = { viewModel.updateUserPassword(it) },
        onValueChangeSecond = { viewModel.updateUserPasswordRepetition(it) },
        onContinueClick = {
            viewModel.nextStep()
        },
        modifier = modifier
    )
}

@Composable
fun CreatePasswordScreen(
    uiState: LoginState,
    passwordRepetition: String,
    onValueChangeFirst: (String) -> Unit,
    onValueChangeSecond: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    TopBarTemplate(
        label = R.string.label_create_account,
        onBackClick = onBackClick,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(ConstColours.BLACK)
                .padding(paddingValues)
//                .windowIn  setsPadding(WindowInsets.systemBars) ,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                if (uiState.isLoading) {
                    LoadingOverlay()
                } else {
                    Text(
                        text = stringResource(R.string.insert_password),
                        color = ConstColours.WHITE,
                        textAlign = TextAlign.Center,
                        style = AppTextStyles.Headlines,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.medium_padding))
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))

                    TextFieldRegistration(
                        value = uiState.userData.password,
                        onValueChange = onValueChangeFirst,
//                        modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                        placeholder = stringResource(R.string.placeholder_password),
                        isError = uiState.isError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))

                    TextFieldRegistration(
                        value = passwordRepetition,
                        onValueChange = onValueChangeSecond,
//                        modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                        placeholder = stringResource(R.string.placeholder_password_repetition),
                        isError = uiState.isError,
                        errorText = handlingErrorLogin(uiState),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))

                    ContinueButton(
                        onClick = onContinueClick,
                        modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                    )
                }
            }
        }
    }
}