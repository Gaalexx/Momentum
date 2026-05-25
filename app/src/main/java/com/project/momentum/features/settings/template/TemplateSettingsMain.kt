package com.project.momentum.features.settings.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.project.momentum.R
import com.project.momentum.features.settings.viewmodel.SettingsEvent
import com.project.momentum.features.settings.viewmodel.SettingsState
import com.project.momentum.navigation.viewmodel.SwitchesState
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.SettingsButtonAdaptive
import com.project.momentum.ui.assets.SwitchRow
import com.project.momentum.ui.assets.ThemeCard
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.ui.theme.MomentumAndroidSettingsTheme
import com.project.momentum.ui.theme.MomentumTheme

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SettingsMainScreenPreview() {
    MomentumTheme {
        TemplateSettingsMain(
            onBackClick = {},
            onEvent = {},
            state = SettingsState(),
            switchesState = SwitchesState(),
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
    switchesState: SwitchesState,
    onHiddenPosts: () -> Unit = {},
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
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
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
                enabled = switchesState.serverSettingsState.inAppNotifications,
                onEnabledChange = { onEvent(SettingsEvent.OnInAppNotifications) }
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_publications),
                enabled = switchesState.serverSettingsState.publicationsEnabled,
                onEnabledChange = { onEvent(SettingsEvent.OnPublicationsEnabled) }
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_reactions),
                enabled = switchesState.serverSettingsState.reactionsEnabled,
                onEnabledChange = { onEvent(SettingsEvent.OnReactionsEnabled) }
            )

            SwitchRow(
                text = stringResource(R.string.settings_friend_requests),
                enabled = switchesState.serverSettingsState.friendRequestEnabled,
                onEnabledChange = { onEvent(SettingsEvent.OnFriendRequestEnabled) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.settings_themes),
                style = AppTextStyles.SubHeadlines,
                color = ConstColours.MAIN_BRAND_BLUE,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    MomentumTheme {
                        ThemeCard(
                            onClick = { onEvent(SettingsEvent.OnDefaultThemeEnabled) }
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f)) {

                    MomentumAndroidSettingsTheme{
                        ThemeCard(
                            onClick = { onEvent(SettingsEvent.OnDefaultThemeEnabled) }
                        )
                    }
                    MomentumTheme{}
                }
            }

            Spacer(modifier = Modifier.weight(10f))

            SettingsButtonAdaptive(
                onClick = onHiddenPosts,
                icon = Icons.Outlined.RemoveRedEye,
                text = stringResource(R.string.settings_hidden_posts),
                textColor = ConstColours.WHITE,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )

            Spacer(modifier = Modifier.weight(0.5f))

            SettingsButtonAdaptive(
                onClick = onPremiumClick,
                icon = Icons.Default.Star,
                text = stringResource(R.string.settings_section_premium),
                textColor = ConstColours.GOLD,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )

            Spacer(modifier = Modifier.weight(0.5f))

            SettingsButtonAdaptive(
                onClick = onLogoutClick,
                icon = Icons.AutoMirrored.Filled.Logout,
                text = stringResource(R.string.settings_section_quit),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )

            Spacer(modifier = Modifier.weight(0.5f))

            SettingsButtonAdaptive(
                onClick = onDeleteAccountClick,
                icon = Icons.Default.Delete,
                text = stringResource(R.string.settings_section_delete),
                textColor = ConstColours.DELETE,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.medium_padding))
            )

            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}
