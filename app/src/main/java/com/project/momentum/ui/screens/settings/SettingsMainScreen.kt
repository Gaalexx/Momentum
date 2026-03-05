package com.project.momentum.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.momentum.ConstColours
import com.project.momentum.R
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.CircleButton
import com.project.momentum.ui.assets.SettingsButton
import com.project.momentum.ui.assets.SwitchRow
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {

    var publicationsEnabled by remember { mutableStateOf(true) }
    var reactionsEnabled by remember { mutableStateOf(true) }
    var inAppNotifications by remember { mutableStateOf(true) }
    var recommendToContacts by remember { mutableStateOf(true) }
    var allowAddFromAnyone by remember { mutableStateOf(true) }
    var confirmBeforePosting by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = ConstColours.BLACK,
            )
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(bottom = 90.dp)
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
                checked = inAppNotifications,
                onCheckedChange = { inAppNotifications = it }
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_publications),
                checked = publicationsEnabled,
                onCheckedChange = { publicationsEnabled = it }
            )

            SwitchRow(
                text = stringResource(R.string.settings_notifications_reactions),
                checked = reactionsEnabled,
                onCheckedChange = { reactionsEnabled = it }
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
                checked = recommendToContacts,
                onCheckedChange = { recommendToContacts = it }
            )

            SwitchRow(
                text = stringResource(R.string.settings_privacy_allow_add_from_anyone),
                checked = allowAddFromAnyone,
                onCheckedChange = { allowAddFromAnyone = it }
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
                checked = confirmBeforePosting,
                onCheckedChange = { confirmBeforePosting = it }
            )

        }


        Spacer(modifier = Modifier.weight(1f))



        SettingsButton(
            onClick = onPremiumClick,
            icon = Icons.Default.Star,
            text = stringResource(R.string.settings_section_premium),
            textColor = ConstColours.GOLD,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        SettingsButton(
            onClick = onLogoutClick,
            icon = Icons.Default.Logout,
            text = stringResource(R.string.settings_section_quit),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        SettingsButton(
            onClick = onDeleteAccountClick,
            icon = Icons.Default.Delete,
            text = stringResource(R.string.settings_section_delete),
            textColor = ConstColours.DELETE,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SettingsMainScreenPreview() {
    MaterialTheme {
        SettingsMainScreen()
    }
}