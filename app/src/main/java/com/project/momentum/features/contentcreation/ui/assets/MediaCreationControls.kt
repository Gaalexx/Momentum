package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.ui.assets.BigCircleForMainScreenAction
import com.project.momentum.ui.assets.BigCircleForMainScreenActionHardCoded
import com.project.momentum.ui.assets.BigCircleMicroButton
import com.project.momentum.ui.assets.BigCircleMicroButtonHardCoded
import com.project.momentum.ui.assets.CircleButtonHardCoded
import com.project.momentum.ui.theme.ConstColours

@Composable
internal fun MediaCreationModeSwitcher(
    mode: ContentCreationMode,
    enabled: Boolean,
    onModeChange: (ContentCreationMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ModeCircleButton(
            selected = mode == ContentCreationMode.Camera,
            enabled = enabled,
            onClick = { onModeChange(ContentCreationMode.Camera) },
            icon = Icons.Outlined.PhotoCamera,
        )
        ModeCircleButton(
            selected = mode == ContentCreationMode.Audio,
            enabled = enabled,
            onClick = { onModeChange(ContentCreationMode.Audio) },
            icon = Icons.Outlined.Mic,
        )
    }
}

@Composable
internal fun CameraModeSwitcher(
    onGoToRecorder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaCreationModeSwitcher(
        mode = ContentCreationMode.Camera,
        enabled = true,
        onModeChange = { mode ->
            if (mode == ContentCreationMode.Audio) {
                onGoToRecorder()
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ModeCircleButton(
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    val activeAlpha = if (enabled) 1f else 0.45f
    CircleButtonHardCoded(
        size = 60.dp,
        onClick = onClick,
        icon = icon,
        backgroundColor = if (selected) ConstColours.BLACK else ConstColours.MAIN_BACK_GRAY,
        iconColor = ConstColours.WHITE.copy(alpha = activeAlpha),
        enabled = enabled,
    )
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


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onToggleTorch,
            enabled = captureEnabled,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .size(50.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.WbSunny,
                contentDescription = stringResource(R.string.icon_flash),
                tint = if (torchEnabled) ConstColours.MAIN_BRAND_BLUE else iconTint,
                modifier = Modifier.size(40.dp),
            )
        }

        Box(modifier = Modifier.weight(1.5f), contentAlignment = Alignment.Center) {
            BigCircleForMainScreenAction(
                onClick = onTakePhoto,
                onLongPressStart = onStartRecording,
                onLongPressEnd = onStopRecording,
                onStartProgress = {},
                onEndProgress = {},
                enabled = captureEnabled,
                progressStarted = captureButtonState,
                modifier = Modifier
                    .aspectRatio(1f)//.align(Alignment.Center),
            )
        }


        IconButton(
            onClick = onFlipCamera,
            enabled = captureEnabled,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
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
internal fun AudioBottomControls(
    enabled: Boolean,
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        BigCircleMicroButton(
            onLongPress = onStartRecording,
            onLongPressEnd = onStopRecording,
            enabled = enabled,
            isRecording = isRecording,
            outerColor = if (enabled) {
                ConstColours.MAIN_BACK_GRAY
            } else {
                ConstColours.MAIN_BACK_GRAY.copy(alpha = 0.45f)
            },
            innerColor = if (enabled) {
                ConstColours.WHITE
            } else {
                Color.White.copy(alpha = 0.55f)
            },
            modifier = Modifier
                .align(Alignment.Center)
                .aspectRatio(1f),
        )
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
