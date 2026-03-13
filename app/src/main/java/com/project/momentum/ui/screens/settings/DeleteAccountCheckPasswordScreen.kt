package com.project.momentum.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.data.registration.NavEvent
import com.project.momentum.ui.screens.login.DeleteAccountViewModel
import com.project.momentum.ui.screens.registration.TemplateDeleteAccountCheckPassword

@Composable
fun DeleteAccountCheckPasswordScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val viewModel: DeleteAccountViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavEvent.NavigateToNextScreen -> onContinueClick()
                else -> onBackClick()
            }
        }
    }

    TemplateDeleteAccountCheckPassword(
        state = uiState,
        onPasswordChange = viewModel::updateUserPassword,
        onBackClick = onBackClick,
        onContinueClick = viewModel::nextStep
    )
}
