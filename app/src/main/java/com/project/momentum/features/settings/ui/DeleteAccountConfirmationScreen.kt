package com.project.momentum.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.settings.template.TemplateDeleteAccountConfirmation
import com.project.momentum.features.settings.viewmodel.DeleteAccountViewModel

@Composable
fun DeleteAccountConfirmationScreen(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeleteAccountViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavEvent.NavigateToNextScreen -> onConfirm()
                else -> onCancel()
            }
        }
    }

    TemplateDeleteAccountConfirmation(
        onEvent = viewModel::onEvent
    )
}
