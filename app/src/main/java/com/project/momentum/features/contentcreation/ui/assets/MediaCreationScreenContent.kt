package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.momentum.features.contentcreation.data.CameraScreenState
import com.project.momentum.ui.theme.ConstColours

@Composable
internal fun CameraLikeContent(
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
