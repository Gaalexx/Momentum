package com.project.momentum.features.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.settings.viewmodel.DeleteAccountViewModel
import com.project.momentum.features.settings.template.TemplateDeleteAccountCheckCode
import com.project.momentum.features.settings.template.TemplateDeleteAccountConfirmation
import com.project.momentum.features.settings.viewmodel.DeleteEvent

@Composable
fun DeleteAccountCheckCodeScreen(
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
    Box(modifier = Modifier.fillMaxSize()) {
        TemplateDeleteAccountCheckCode(
            state = uiState,
            onBackClick = {
                viewModel.onEvent(DeleteEvent.previousStep)
                onBackClick()
            },
            onEvent = viewModel::onEvent,
            onContinueClick = {
                viewModel.onEvent(DeleteEvent.nextStep)
                //onContinueClick()
            }
        )
        if (uiState.showConfirmationDialog) {
            Dialog(
                onDismissRequest = { viewModel.onEvent(DeleteEvent.onCancelDelete) },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false
                )
            ) {
                TemplateDeleteAccountConfirmation(
                    onEvent = viewModel::onEvent
                )
            }
        }
    }

}
