package com.project.momentum.features.settings.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.momentum.R
import com.project.momentum.features.settings.viewmodel.SettingsMainScreenViewModel
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.SettingsButton
import com.project.momentum.ui.assets.SwitchRow
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours


@Composable
fun GetAllStatesOfSwitches(
    getInAppNotifications: () -> Unit = {},
    getPublicationsEnabled: () -> Unit = {},
    getReactionsEnabled: () -> Unit = {},
    getRecommendToContacts: () -> Unit = {},
    getAllowAddFromAnyone: () -> Unit = {},
    getConfirmBeforePosting: () -> Unit = {},
) {
    getInAppNotifications()
    getPublicationsEnabled()
    getReactionsEnabled()
    getRecommendToContacts()
    getAllowAddFromAnyone()
    getConfirmBeforePosting()
}