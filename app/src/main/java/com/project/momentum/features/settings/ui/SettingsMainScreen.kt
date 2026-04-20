package com.project.momentum.features.settings.ui

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.settings.models.dto.ServerSettingsStateDTO
import com.project.momentum.features.settings.template.TemplateSettingsMain
import com.project.momentum.features.settings.viewmodel.SettingsEffect
import com.project.momentum.features.settings.viewmodel.SettingsMainScreenViewModel
import com.project.momentum.navigation.viewmodel.AppStartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit,
    onPremiumClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    viewModel: SettingsMainScreenViewModel = hiltViewModel(),
    appStartViewModel: AppStartViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val serverState by appStartViewModel.serverSettings.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.bind(appStartViewModel)
        viewModel.effects.collect { effect ->
            when (effect) {
                is SettingsEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        TemplateSettingsMain(
            onBackClick = onBackClick,
            onEvent = { event -> viewModel.onEvent(event, appStartViewModel) },
            state = uiState,
            serverState = serverState ?: ServerSettingsStateDTO(),
            onPremiumClick = onPremiumClick,
            onLogoutClick = onLogoutClick,
            onDeleteAccountClick = onDeleteAccountClick
        )
    }
}
