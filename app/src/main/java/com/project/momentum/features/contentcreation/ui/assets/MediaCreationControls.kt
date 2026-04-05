package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.ui.assets.BigCircleForMainScreenAction
import com.project.momentum.ui.assets.CircleButton
import com.project.momentum.ui.theme.ConstColours

@Composable
internal fun CameraModeSwitcher(
    onGoToRecorder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleButton(
            size = 60.dp,
            onClick = {},
            icon = Icons.Outlined.PhotoCamera,
            backgroundColor = ConstColours.BLACK,
        )
        CircleButton(
            size = 60.dp,
            onClick = onGoToRecorder,
            icon = Icons.Outlined.Mic,
        )
    }
}

@Composable
internal fun CameraBottomControls(
    torchEnabled: Boolean,
    captureEnabled: Boolean,
    captureButtonState: MutableState<Boolean>,
    onToggleTorch: () -> Unit,
    onTakePhoto: () -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onFlipCamera: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconTint = if (captureEnabled) {
        ConstColours.WHITE
    } else {
        ConstColours.WHITE.copy(alpha = 0.4f)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = onToggleTorch,
            enabled = captureEnabled,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(50.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.WbSunny,
                contentDescription = stringResource(R.string.icon_flash),
                tint = if (torchEnabled) ConstColours.MAIN_BRAND_BLUE else iconTint,
                modifier = Modifier.size(40.dp),
            )
        }

        BigCircleForMainScreenAction(
            onClick = onTakePhoto,
            onLongPressStart = onStartRecording,
            onLongPressEnd = onStopRecording,
            onStartProgress = {},
            onEndProgress = {},
            enabled = captureEnabled,
            progressStarted = captureButtonState,
            modifier = Modifier.align(Alignment.Center),
        )

        IconButton(
            onClick = onFlipCamera,
            enabled = captureEnabled,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(50.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Cached,
                contentDescription = stringResource(R.string.icon_flip_camera),
                tint = iconTint,
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Composable
internal fun GalleryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowUp,
            contentDescription = stringResource(R.string.gallery_title),
            tint = ConstColours.WHITE.copy(alpha = 0.6f),
            modifier = Modifier.size(34.dp),
        )
    }
}
