package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton

@Composable
internal fun CameraTopBar(
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        ProfileCircleButton(
            onClick = onProfileClick,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        FriendsPillButton(
            onClick = onGoToFriends,
            modifier = Modifier.align(Alignment.Center),
        )
        SettingsCircleButton(
            onClick = onGoToSettings,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}
