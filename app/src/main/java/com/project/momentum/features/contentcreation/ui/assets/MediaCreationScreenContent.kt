package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.momentum.features.contentcreation.state.CameraScreenState
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.ui.theme.ConstColours

@Composable
internal fun MediaCreationContent(
    mode: ContentCreationMode,
    hasCameraPermission: Boolean,
    hasMicrophonePermission: Boolean,
    cameraState: CameraScreenState,
    cameraRecordingProgress: Float,
    cameraCaptureButtonState: MutableState<Boolean>,
    audioRecordingProgress: Float,
    audioLevel: Float,
    isAudioRecording: Boolean,
    modeSwitchEnabled: Boolean,
    onModeChange: (ContentCreationMode) -> Unit,
    onTakePhoto: () -> Unit,
    onStartVideoRecording: () -> Unit,
    onStopVideoRecording: () -> Unit,
    onStartAudioRecording: () -> Unit,
    onStopAudioRecording: () -> Unit,
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
            MediaCreationPreviewCard(
                mode = mode,
                hasCameraPermission = hasCameraPermission,
                hasMicrophonePermission = hasMicrophonePermission,
                cameraState = cameraState,
                progress = when (mode) {
                    ContentCreationMode.Camera -> cameraRecordingProgress
                    ContentCreationMode.Audio -> audioRecordingProgress
                },
                audioLevel = audioLevel,
            )
            MediaCreationModeSwitcher(
                mode = mode,
                enabled = modeSwitchEnabled,
                onModeChange = onModeChange,
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
            when (mode) {
                ContentCreationMode.Camera -> {
                    CameraBottomControls(
                        torchEnabled = cameraState.torchEnabled,
                        captureEnabled = hasCameraPermission,
                        captureButtonState = cameraCaptureButtonState,
                        onToggleTorch = cameraState::toggleTorch,
                        onTakePhoto = onTakePhoto,
                        onStartRecording = onStartVideoRecording,
                        onStopRecording = onStopVideoRecording,
                        onFlipCamera = cameraState::flipCamera,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                    )
                }

                ContentCreationMode.Audio -> {
                    AudioBottomControls(
                        enabled = hasMicrophonePermission,
                        isRecording = isAudioRecording,
                        onStartRecording = onStartAudioRecording,
                        onStopRecording = onStopAudioRecording,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                    )
                }
            }

            GalleryButton(onClick = onGoToGallery)
        }
    }
}


@Composable
internal fun MediaCreationContentCompact(
    mode: ContentCreationMode,
    hasCameraPermission: Boolean,
    hasMicrophonePermission: Boolean,
    cameraState: CameraScreenState,
    cameraRecordingProgress: Float,
    cameraCaptureButtonState: MutableState<Boolean>,
    audioRecordingProgress: Float,
    audioLevel: Float,
    isAudioRecording: Boolean,
    modeSwitchEnabled: Boolean,
    onModeChange: (ContentCreationMode) -> Unit,
    onTakePhoto: () -> Unit,
    onStartVideoRecording: () -> Unit,
    onStopVideoRecording: () -> Unit,
    onStartAudioRecording: () -> Unit,
    onStopAudioRecording: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.BLACK)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {


        MediaCreationPreviewCard(
            mode = mode,
            hasCameraPermission = hasCameraPermission,
            hasMicrophonePermission = hasMicrophonePermission,
            cameraState = cameraState,
            progress = when (mode) {
                ContentCreationMode.Camera -> cameraRecordingProgress
                ContentCreationMode.Audio -> audioRecordingProgress
            },
            audioLevel = audioLevel,
        )

        MediaCreationModeSwitcher(
            mode = mode,
            enabled = modeSwitchEnabled,
            onModeChange = onModeChange,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.7f),
        )

        Spacer(modifier = Modifier.weight(0.6f))

        when (mode) {
            ContentCreationMode.Camera -> {
                CameraBottomControls(
                    torchEnabled = cameraState.torchEnabled,
                    captureEnabled = hasCameraPermission,
                    captureButtonState = cameraCaptureButtonState,
                    onToggleTorch = cameraState::toggleTorch,
                    onTakePhoto = onTakePhoto,
                    onStartRecording = onStartVideoRecording,
                    onStopRecording = onStopVideoRecording,
                    onFlipCamera = cameraState::flipCamera,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                        .weight(2f),
                )
            }

            ContentCreationMode.Audio -> {
                AudioBottomControls(
                    enabled = hasMicrophonePermission,
                    isRecording = isAudioRecording,
                    onStartRecording = onStartAudioRecording,
                    onStopRecording = onStopAudioRecording,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                        .weight(2f),
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            GalleryButton(modifier = Modifier, onClick = {})
        }
    }
}

