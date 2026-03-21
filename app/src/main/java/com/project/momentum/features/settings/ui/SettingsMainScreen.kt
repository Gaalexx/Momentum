package com.project.momentum.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.settings.template.TemplateSettingsMain
import com.project.momentum.features.settings.viewmodel.SettingsMainScreenViewModel

@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit,
    onPremiumClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    viewModel: SettingsMainScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    TemplateSettingsMain(
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent,
        state = uiState,
        onPremiumClick = onPremiumClick,
        onLogoutClick = onLogoutClick,
        onDeleteAccountClick = onDeleteAccountClick
    )
}
