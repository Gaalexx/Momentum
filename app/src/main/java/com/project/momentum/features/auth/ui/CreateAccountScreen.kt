package com.project.momentum.features.auth.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.R
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.auth.viewmodel.RegistrationViewModel
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.common.LoadingOverlay

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

    Box{
        if (uiState.isLoading) {
            LoadingOverlay()
        } else {
            TemplateAuthorizationScreen(
                value = uiState.userData.email,
                label = R.string.label_create_account,
                title = R.string.insert_email,
                subButtonText = R.string.button_already_have_account,
                onValueChange = { viewModel.updateUserEmail(it) },
                onBackClick = {
                    viewModel.previousStep() // TODO: if canGoBack == false => не показывать кнопку
                    onBackClick()
                },
                onContinueClick = {
                    viewModel.nextStep()
                },
                onSubButtonClick = onSubButtonClick,
                modifier = modifier,
                isError = uiState.isError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
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
        }
    }
}
