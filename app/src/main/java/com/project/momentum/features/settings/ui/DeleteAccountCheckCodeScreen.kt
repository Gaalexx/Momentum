package com.project.momentum.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.settings.viewmodel.DeleteAccountViewModel
import com.project.momentum.features.settings.template.TemplateDeleteAccountCheckCode

@Composable
fun DeleteAccountCheckCodeScreen(
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

    TemplateDeleteAccountCheckCode(
        state = uiState,
        onPasswordChange = viewModel::updateUserPassword,
        onBackClick = onBackClick,
        onDeleteClick = viewModel::nextStep
    )
}
