package com.project.momentum.ui.screens.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.momentum.ConstColours
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.R
import com.project.momentum.data.LoginType
import com.project.momentum.data.registration.NavEvent
import com.project.momentum.ui.assets.SubButton
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    CreateAccountScreen(
        onBackClick = {},
        onContinueClick = {},
        onSubButtonClick = {}
    )
}

@Composable
fun CreateAccountScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSubButtonClick: () -> Unit,
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

    TopBarTemplate(
        label = R.string.label_create_account,
        onBackClick = {
            viewModel.previousStep()
            onBackClick()
        },
        modifier = modifier
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .background(ConstColours.BLACK)
                .padding(paddingValues)
//                .windowInsetsPadding(WindowInsets.systemBars) ,
        ) {
            if (uiState.isLoading) {
                LoadingOverlay()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.weight(1f))

                    Box {
                        Image(
                            painter = painterResource(R.drawable.profile_image_small),
                            contentDescription = null,
                            modifier = Modifier
                                .height(120.dp)
                                .aspectRatio(1f),
                        )
                        Image(
                            painter = painterResource(R.drawable.add_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(dimensionResource(R.dimen.small_padding))
                                .size(20.dp)
                                .border(
                                    width = 2.dp,
                                    color = ConstColours.BLACK,
                                    shape = CircleShape
                                )
                        )
                    }
                    Spacer(Modifier.weight(4f))
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldRegistration(
                        value = uiState.userData.email,
                        onValueChange = { viewModel.updateUserEmail(it) },
                        modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                        placeholder = "Введеите почту",
                        isError = uiState.isError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType =
                                when (uiState.loginType) {
                                    LoginType.EMAIL -> KeyboardType.Email
                                    else -> KeyboardType.Phone
                                },
                            imeAction = ImeAction.Done
                        ),
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))

                    ContinueButton(
                        onClick = {
                            viewModel.nextStep()
                        },
                        modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                    )

                    SubButton(
                        text = R.string.button_already_have_account,
                        onClick = onSubButtonClick
                    )
                }
            }
        }
    }
}