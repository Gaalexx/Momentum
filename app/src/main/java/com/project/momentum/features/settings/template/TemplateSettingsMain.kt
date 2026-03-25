package com.project.momentum.features.settings.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.project.momentum.R
import com.project.momentum.features.settings.viewmodel.SettingsEvent
import com.project.momentum.features.settings.viewmodel.SettingsState
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.SettingsButton
import com.project.momentum.ui.assets.SwitchRow
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SettingsMainScreenPreview() {
    MaterialTheme {
        TemplateSettingsMain(
            onBackClick = {},
            onEvent = {},
            state = SettingsState(),
            onPremiumClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}

@Composable
fun TemplateSettingsMain(
    onBackClick: () -> Unit = {},
    onEvent: (SettingsEvent)-> Unit,
    state: SettingsState?,
    onPremiumClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
) {
    if (state == null || state.isLoading) {
        LoadingOverlay()
        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = ConstColours.BLACK,
            )
            .windowInsetsPadding(WindowInsets.systemBars)

    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            BackCircleButton(
                onClick = onBackClick
            )

            Text(
                text = stringResource(R.string.settings_main_screen_headliner),
                style = AppTextStyles.Headlines,
                color = ConstColours.WHITE,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_notifications_section),
                style = AppTextStyles.SubHeadlines,
                color = ConstColours.MAIN_BRAND_BLUE,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_in_app_switch),
                enabled = state.serverSettingsState.inAppNotifications,
                onEnabledChange = { onEvent(SettingsEvent.onInAppNotifications) }
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_publications),
                enabled = state.serverSettingsState.publicationsEnabled,
                onEnabledChange = { onEvent(SettingsEvent.onPublicationsEnabled) }
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_reactions),
                enabled = state.serverSettingsState.reactionsEnabled,
                onEnabledChange = { onEvent(SettingsEvent.onReactionsEnabled) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.settings_privacy_account_visibility),
                style = AppTextStyles.SubHeadlines,
                color = ConstColours.MAIN_BRAND_BLUE,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SwitchRow(
                text = stringResource(R.string.settings_privacy_recommend_to_contacts),
                enabled = state.serverSettingsState.recommendToContacts,
                onEnabledChange = { onEvent(SettingsEvent.onRecommendToContacts) }
            )

            SwitchRow(
                text = stringResource(R.string.settings_privacy_allow_add_from_anyone),
                enabled = state.serverSettingsState.allowAddFromAnyone,
                onEnabledChange = { onEvent(SettingsEvent.onAllowAddFromAnyone) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.settings_privacy_caution),
                style = AppTextStyles.SubHeadlines,
                color = ConstColours.MAIN_BRAND_BLUE,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SwitchRow(
                text = stringResource(R.string.settings_privacy_confirm_before_posting),
                enabled = state.localSettingsState.confirmBeforePosting,
                onEnabledChange = { onEvent(SettingsEvent.onConfirmBeforePosting) }
            )

            Spacer(modifier = Modifier.weight(1f))



            SettingsButton(
                onClick = onPremiumClick,
                icon = Icons.Default.Star,
                text = stringResource(R.string.settings_section_premium),
                textColor = ConstColours.GOLD,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1f))

            SettingsButton(
                onClick = onLogoutClick,
                icon = Icons.AutoMirrored.Filled.Logout,
                text = stringResource(R.string.settings_section_quit),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1f))

            SettingsButton(
                onClick = onDeleteAccountClick,
                icon = Icons.Default.Delete,
                text = stringResource(R.string.settings_section_delete),
                textColor = ConstColours.DELETE,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}