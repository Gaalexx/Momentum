package com.project.momentum.features.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.features.settings.features.GetAllStatesOfSwitches
import com.project.momentum.features.settings.template.TemplateDeleteAccountConfirmation
import com.project.momentum.features.settings.template.TemplateSettingsMain
import com.project.momentum.features.settings.viewmodel.DeleteAccountViewModel
import com.project.momentum.features.settings.viewmodel.SettingsMainScreenViewModel

@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit,
    onPremiumClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    val viewModel: SettingsMainScreenViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsState()

    GetAllStatesOfSwitches(
        getInAppNotifications = viewModel::getInAppNotifications,
        getPublicationsEnabled = viewModel::getPublicationsEnabled,
        getReactionsEnabled = viewModel::getReactionsEnabled,
        getRecommendToContacts = viewModel::getRecommendToContacts,
        getAllowAddFromAnyone = viewModel::getAllowAddFromAnyone,
        getConfirmBeforePosting = viewModel::getConfirmBeforePosting
    )

    TemplateSettingsMain(
        onBackClick = onBackClick,
        onInAppNotifications = viewModel::onInAppNotifications,
        onPublicationsEnabled = viewModel::onPublicationsEnabled,
        onReactionsEnabled = viewModel::onReactionsEnabled,
        onRecommendToContacts = viewModel::onRecommendToContacts,
        onAllowAddFromAnyone = viewModel::onAllowAddFromAnyone,
        onConfirmBeforePosting = viewModel::onConfirmBeforePosting,
        state = uiState,
        onPremiumClick = onPremiumClick,
        onLogoutClick = onLogoutClick,
        onDeleteAccountClick = onDeleteAccountClick
    )
}
