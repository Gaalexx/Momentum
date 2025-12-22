package com.project.momentum

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import com.example.momentum.ConstColours
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun SettingsMainScreen(
    onBackClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onDataClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = ConstColours.BLACK,
            )
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
                .height(81.dp)
        ) {
            BackCircleButton(
                onClick = onBackClick,
                size = 36.dp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
            )

            CircleButton(
                onClick = {},
                icon = Icons.Default.Settings,
                size = 81.dp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .padding(bottom = 8.dp, start = 24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings_main_screen_headliner),
                style = AppTextStyles.Headlines,
                color = ConstColours.WHITE
            )
        }

        SettingsButton(
            onClick = onPrivacyClick,
            icon = Icons.Default.Security,
            text = stringResource(R.string.settings_section_privacy),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        SettingsButton(
            onClick = onNotificationsClick,
            icon = Icons.Default.Notifications,
            text = stringResource(R.string.settings_section_notifications),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        SettingsButton(
            onClick = onDataClick,
            icon = Icons.Default.Storage,
            text = stringResource(R.string.settings_section_data),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clickable { onLanguageClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                stringResource(R.string.settings_section_language),
                color = ConstColours.WHITE,
                style = AppTextStyles.MainText,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(
                        color = ConstColours.MAIN_BACK_GRAY,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    stringResource(R.string.settings_type_language),
                    color = ConstColours.WHITE,
                    style = AppTextStyles.MainText
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.settings_language_description),
                    tint = ConstColours.WHITE,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        SettingsButton(
            onClick = onPremiumClick,
            icon = Icons.Default.Star,
            text = stringResource(R.string.settings_section_premium),
            textColor = ConstColours.GOLD,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

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

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SettingsMainScreenPreview() {
    MaterialTheme {
        SettingsMainScreen()
    }
}