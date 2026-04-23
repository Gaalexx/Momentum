package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.momentum.ui.assets.FriendsPillButtonAdaptive
import com.project.momentum.ui.assets.ProfileCircleButtonAdaptive
import com.project.momentum.ui.assets.SettingsCircleButtonAdaptive

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
        ProfileCircleButtonAdaptive(
            modifier = Modifier
                .fillMaxHeight(0.97f)
                .aspectRatio(1f)
                .align(Alignment.CenterStart),
            onClick = onProfileClick,
        )
        FriendsPillButtonAdaptive(
            onClick = onGoToFriends,
            modifier = Modifier
                .fillMaxHeight(0.97f)
                .fillMaxWidth(0.45f)
                .align(Alignment.Center),
        )
        SettingsCircleButtonAdaptive(
            onClick = onGoToSettings,
            modifier = Modifier
                .fillMaxHeight(0.97f)
                .aspectRatio(1f)
                .align(Alignment.CenterEnd),
        )
    }
}
