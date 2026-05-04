package com.project.momentum.features.contentcreation.ui

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.features.contentcreation.media.rememberAudioRecordingController
import com.project.momentum.features.contentcreation.media.rememberCameraCaptureActions
import com.project.momentum.features.contentcreation.media.rememberCameraRecordingController
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import com.project.momentum.features.contentcreation.permissions.rememberCameraPermissionState
import com.project.momentum.features.contentcreation.permissions.rememberMicrophonePermissionState
import com.project.momentum.features.contentcreation.state.rememberCameraScreenState
import com.project.momentum.features.contentcreation.ui.assets.MediaCreationContent
import com.project.momentum.features.contentcreation.ui.assets.MediaCreationContentCompact

const val DefaultMaxRecordMs = 60_000

@Composable
fun MediaCreationScreen(
    modifier: Modifier = Modifier,
    initialMode: ContentCreationMode = ContentCreationMode.Camera,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    maxRecordMs: Int = DefaultMaxRecordMs,
) {
    var mode by rememberSaveable { mutableStateOf(initialMode) }
    val hasCameraPermission by rememberCameraPermissionState(
        shouldRequest = mode == ContentCreationMode.Camera,
    )
    val hasMicrophonePermission by rememberMicrophonePermissionState(
        shouldRequest = mode == ContentCreationMode.Audio,
    )

    val cameraState = rememberCameraScreenState()
    val captureActions = rememberCameraCaptureActions()
    val cameraRecordingController = rememberCameraRecordingController(
        state = cameraState,
        captureActions = captureActions,
        maxRecordMs = maxRecordMs,
    )
    val audioRecordingController = rememberAudioRecordingController(
        maxRecordMs = maxRecordMs,
    )

    DisposableEffect(cameraRecordingController, audioRecordingController) {
        onDispose {
            cameraRecordingController.dispose()
            audioRecordingController.dispose()
        }
    }

    val isCaptureActive = cameraState.hasActiveRecording || audioRecordingController.isRecording

    MediaCreationContentCompact(
        modifier = modifier,
        mode = mode,
        hasCameraPermission = hasCameraPermission,
        hasMicrophonePermission = hasMicrophonePermission,
        cameraState = cameraState,
        cameraRecordingProgress = cameraRecordingController.progress.value,
        cameraCaptureButtonState = cameraRecordingController.captureButtonState,
        audioRecordingProgress = audioRecordingController.progress,
        audioLevel = audioRecordingController.amplitudeLevel,
        isAudioRecording = audioRecordingController.isRecording,
        modeSwitchEnabled = !isCaptureActive,
        onModeChange = { nextMode ->
            if (!isCaptureActive) {
                mode = nextMode
            }
        },
        onTakePhoto = {
            captureActions.takePhoto(
                imageCapture = cameraState.imageCapture,
                isFrontCamera = cameraState.isFrontCamera,
                onSaved = onGoToPreview,
            )
        },
        onStartVideoRecording = {
            cameraRecordingController.start(onSaved = onGoToPreview)
        },
        onStopVideoRecording = {
            cameraRecordingController.stop()
        },
        onStartAudioRecording = {
            if (hasMicrophonePermission) {
                audioRecordingController.start(onSaved = onGoToPreview)
            }
        },
        onStopAudioRecording = {
            audioRecordingController.stop(onSaved = onGoToPreview)
        },
        onProfileClick = onProfileClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
    )
}

@Composable
fun CameraLikeScreen(
    modifier: Modifier = Modifier,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onGoToRecorder: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToGallery: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    maxRecordMs: Int = DefaultMaxRecordMs,
) {
    MediaCreationScreen(
        modifier = modifier,
        initialMode = ContentCreationMode.Camera,
        onGoToPreview = onGoToPreview,
        onProfileClick = onProfileClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
        maxRecordMs = maxRecordMs,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun MediaCreationScreenPreview() {
    MaterialTheme {
        MediaCreationContent(
            mode = ContentCreationMode.Camera,
            hasCameraPermission = false,
            hasMicrophonePermission = true,
            cameraState = rememberCameraScreenState(),
            cameraRecordingProgress = 0f,
            cameraCaptureButtonState = remember { mutableStateOf(false) },
            audioRecordingProgress = 0f,
            audioLevel = 0.5f,
            isAudioRecording = false,
            modeSwitchEnabled = true,
            onModeChange = {},
            onTakePhoto = {},
            onStartVideoRecording = {},
            onStopVideoRecording = {},
            onStartAudioRecording = {},
            onStopAudioRecording = {},
            onProfileClick = {},
            onGoToGallery = {},
            onGoToSettings = {},
            onGoToFriends = {},
        )
    }
}
