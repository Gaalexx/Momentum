package com.project.momentum.features.contentcreation.ui

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.features.contentcreation.data.MediaTypeToSend
import com.project.momentum.features.contentcreation.data.rememberCameraPermissionState
import com.project.momentum.features.contentcreation.data.rememberCameraScreenState
import com.project.momentum.features.contentcreation.ui.assets.CameraLikeContent

private const val DefaultMaxRecordMs = 10_000

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
        onStartRecording = { recordingController.start(onSaved = onGoToPreview) },
        onStopRecording = {

            recordingController.stop()
        },
        onGoToRecorder = onGoToRecorder,
        onProfileClick = onProfileClick,
        onGoToGallery = onGoToGallery,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
    )
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
