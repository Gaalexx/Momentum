package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.momentum.ui.assets.FriendsPillButtonHardCoded
import com.project.momentum.ui.assets.ProfileCircleButtonHardCoded
import com.project.momentum.ui.assets.SettingsCircleButtonHardCoded

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
        ProfileCircleButtonHardCoded(
            onClick = onProfileClick,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        FriendsPillButtonHardCoded(
            onClick = onGoToFriends,
            modifier = Modifier.align(Alignment.Center),
        )
        SettingsCircleButtonHardCoded(
            onClick = onGoToSettings,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}
