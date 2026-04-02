package com.project.momentum.features.contentcreation.ui

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.ui.assets.BigCircleForMainScreenAction
import com.project.momentum.ui.assets.CircleButton
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton
import com.project.momentum.ui.theme.ConstColours

private val PreviewCardShape = RoundedCornerShape(60.dp)
private const val DefaultMaxRecordMs = 10_000
private const val RecordingProgressStartShiftFraction = 0.3425f

@Composable
fun CameraLikeScreen(
    modifier: Modifier = Modifier,
    onGoToPreview: (Uri) -> Unit,
    onGoToRecorder: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToGallery: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    maxRecordMs: Int = DefaultMaxRecordMs,
) {
    val hasCameraPermission by rememberCameraPermissionState()
    val state = rememberCameraScreenState()
    val captureActions = rememberCameraCaptureActions()
    val recordingController = rememberCameraRecordingController(
        state = state,
        captureActions = captureActions,
        maxRecordMs = maxRecordMs,
    )

    DisposableEffect(recordingController) {
        onDispose {
            recordingController.dispose()
        }
    }

    CameraLikeContent(
        modifier = modifier,
        hasCameraPermission = hasCameraPermission,
        state = state,
        recordingProgress = recordingController.progress.value,
        captureButtonState = recordingController.captureButtonState,
        onTakePhoto = {
            captureActions.takePhoto(
                imageCapture = state.imageCapture,
                isFrontCamera = state.isFrontCamera,
                onSaved = onGoToPreview,
            )
        },
        onStartRecording = recordingController::start,
        onStopRecording = { recordingController.stop() },
        onGoToRecorder = onGoToRecorder,
        onProfileClick = onProfileClick,
        onGoToGallery = onGoToGallery,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
    )
}

@Composable
private fun CameraLikeContent(
    hasCameraPermission: Boolean,
    state: CameraScreenState,
    recordingProgress: Float,
    captureButtonState: MutableState<Boolean>,
    onTakePhoto: () -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onGoToRecorder: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToGallery: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.BLACK)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        CameraTopBar(
            onProfileClick = onProfileClick,
            onGoToSettings = onGoToSettings,
            onGoToFriends = onGoToFriends,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 82.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CameraPreviewCard(
                hasCameraPermission = hasCameraPermission,
                state = state,
                progress = recordingProgress,
            )
            CameraModeSwitcher(
                onGoToRecorder = onGoToRecorder,
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CameraBottomControls(
                torchEnabled = state.torchEnabled,
                captureEnabled = hasCameraPermission,
                captureButtonState = captureButtonState,
                onToggleTorch = state::toggleTorch,
                onTakePhoto = onTakePhoto,
                onStartRecording = onStartRecording,
                onStopRecording = onStopRecording,
                onFlipCamera = state::flipCamera,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp),
            )
            GalleryButton(onClick = onGoToGallery)
        }
    }
}

@Composable
private fun CameraTopBar(
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

@Composable
private fun CameraPreviewCard(
    hasCameraPermission: Boolean,
    state: CameraScreenState,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(PreviewCardShape)
            .background(ConstColours.BLACK),
    ) {
        RecordingBorderProgress(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .clip(PreviewCardShape)
                .background(ConstColours.MAIN_BACK_GRAY)
                .align(Alignment.Center),
        ) {
            if (hasCameraPermission) {
                CameraPreviewContainer(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                CameraPermissionPlaceholder()
            }
        }
    }
}

@Composable
private fun RecordingBorderProgress(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val progressPath = remember { PathMeasure() }

    Canvas(modifier = modifier) {
        val fullPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        topLeft = Offset.Zero,
                        bottomRight = Offset(size.width, size.height),
                    ),
                    cornerRadius = CornerRadius(65.dp.toPx()),
                ),
                direction = Path.Direction.Clockwise,
            )
        }

        progressPath.setPath(fullPath, forceClosed = true)
        val totalLength = progressPath.length
        val start = totalLength * RecordingProgressStartShiftFraction
        val visibleLength = totalLength * progress
        val end = start + visibleLength
        val segmentPath = Path()

        if (end <= totalLength) {
            progressPath.getSegment(
                startDistance = start,
                stopDistance = end,
                destination = segmentPath,
                startWithMoveTo = true,
            )
        } else {
            progressPath.getSegment(
                startDistance = start,
                stopDistance = totalLength,
                destination = segmentPath,
                startWithMoveTo = true,
            )

            val secondPart = Path()
            progressPath.getSegment(
                startDistance = 0f,
                stopDistance = end - totalLength,
                destination = secondPart,
                startWithMoveTo = true,
            )
            segmentPath.addPath(secondPart)
        }

        drawPath(
            path = segmentPath,
            color = ConstColours.WHITE,
            style = Stroke(width = 8.dp.toPx()),
        )
    }
}

@Composable
private fun CameraPermissionPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.PhotoCamera,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(56.dp),
        )
    }
}

@Composable
private fun CameraModeSwitcher(
    onGoToRecorder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Row(
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
private fun CameraBottomControls(
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
private fun GalleryButton(
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

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun CameraLikeScreenPreview() {
    MaterialTheme {
        CameraLikeContent(
            hasCameraPermission = false,
            state = rememberCameraScreenState(),
            recordingProgress = 0f,
            captureButtonState = remember { mutableStateOf(false) },
            onTakePhoto = {},
            onStartRecording = {},
            onStopRecording = {},
            onGoToRecorder = {},
            onProfileClick = {},
            onGoToGallery = {},
            onGoToSettings = {},
            onGoToFriends = {},
        )
    }
}
