package com.project.momentum.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.settings.template.TemplateDeleteAccountCheckPassword
import com.project.momentum.features.settings.viewmodel.DeleteAccountViewModel

@Composable
fun DeleteAccountCheckPasswordScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    viewModel: DeleteAccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

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
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent,
    )
}
