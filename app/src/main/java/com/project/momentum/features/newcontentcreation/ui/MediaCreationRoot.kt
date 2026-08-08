package com.project.momentum.features.newcontentcreation.ui

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import com.project.momentum.features.contentcreation.permissions.rememberCameraPermissionState
import com.project.momentum.features.contentcreation.permissions.rememberMicrophonePermissionState
import com.project.momentum.features.contentcreation.ui.DefaultMaxRecordMs
import com.project.momentum.features.newcontentcreation.mediaconfig.AudioRecordingFormat
import com.project.momentum.features.newcontentcreation.mediaconfig.PhotoRecordingFormat
import com.project.momentum.features.newcontentcreation.mediaconfig.VideoRecordingFormat
import com.project.momentum.features.newcontentcreation.ui.assets.AudioBottomControls
import com.project.momentum.features.newcontentcreation.ui.assets.GalleryButton
import com.project.momentum.features.newcontentcreation.ui.assets.MyCameraBottomControls
import com.project.momentum.features.newcontentcreation.ui.assets.MyMediaCreationModeSwitcher
import com.project.momentum.features.newcontentcreation.ui.assets.MyMediaCreationPreviewCard
import com.project.momentum.features.newcontentcreation.viewmodel.CameraEvent
import com.project.momentum.features.newcontentcreation.viewmodel.CameraState
import com.project.momentum.features.newcontentcreation.viewmodel.NewCameraViewModel
import com.project.momentum.ui.theme.ConstColours
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MyMediaCreationRoot(
    modifier: Modifier = Modifier,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    contentCreationViewModel: NewCameraViewModel = hiltViewModel()
) {
    val vmState = contentCreationViewModel.state.collectAsStateWithLifecycle().value
    val controller = contentCreationViewModel.controller
    val onEvent = contentCreationViewModel::onEvent

    MyMediaCreationScreen(
        modifier = modifier,
        onGoToPreview = onGoToPreview,
        onProfileClick = onProfileClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
        cameraState = vmState,
        onEvent = onEvent,
        controller = controller
    )
}

@Composable
fun MyMediaCreationScreen(
    modifier: Modifier = Modifier,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    cameraState: CameraState,
    onEvent: (CameraEvent) -> Unit,
    controller: LifecycleCameraController,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val hasCameraPermission by rememberCameraPermissionState(
        shouldRequest = true
    )
    val hasMicrophonePermission by rememberMicrophonePermissionState(
        shouldRequest = true
    )

    val progress = remember { Animatable(0f) }

    LaunchedEffect(cameraState.isRecording) {
        if (cameraState.isRecording) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = DefaultMaxRecordMs,
                    easing = LinearEasing,
                ),
            )
            progress.snapTo(0f)
            val result = CompletableDeferred<Uri?>()
            onEvent(CameraEvent.OnStopAllRecords(result))
            val uri = result.await()
            if (uri != null) {
                onGoToPreview(
                    uri,
                    if (cameraState.contentCreationMode == ContentCreationMode.Camera) MediaTypeToSend.VIDEO else MediaTypeToSend.AUDIO
                )
            } else {
                Toast.makeText(context, "Error with media recording occurred", Toast.LENGTH_LONG)
                    .show()
            }

        } else {
            progress.snapTo(0f)
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.BLACK),
    ) {
        MyMediaCreationPreviewCard(
            mode = cameraState.contentCreationMode,
            hasCameraPermission = hasCameraPermission,
            hasMicrophonePermission = hasMicrophonePermission,
            progress = progress.value,
            audioLevel = 0.0F/* audioRecording?. */,
            cameraPreviewEnabled = true,
            controller = controller
        )

        MyMediaCreationModeSwitcher(
            mode = cameraState.contentCreationMode,
            enabled = !cameraState.isRecording,
            onModeChange = { it ->
                onEvent(CameraEvent.OnContentCreationModeChange(it))
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.7f),
        )

        Spacer(modifier = Modifier.weight(0.3f))

        when (cameraState.contentCreationMode) {
            ContentCreationMode.Camera -> {

                var pendingRecording by remember { mutableStateOf<CompletableDeferred<Uri?>?>(null) }

                MyCameraBottomControls(
                    torchEnabled = cameraState.torchEnabled,
                    captureEnabled = hasCameraPermission,
                    captureButtonState = cameraState.isRecording,//cameraCaptureButtonState,
                    onToggleTorch = { onEvent(CameraEvent.OnToggleFlash) },
                    onTakePhoto = {
                        scope.launch {
                            val result = CompletableDeferred<Uri>()
                            onEvent(CameraEvent.OnTakePhoto(result))
                            val uri = result.await()
                            onGoToPreview(uri, MediaTypeToSend.PHOTO)
                        }
                    },
                    onStartRecording = {
                        scope.launch {
                            val result = CompletableDeferred<Uri?>()
                            pendingRecording = result
                            onEvent(CameraEvent.OnRecordVideoSwitch(result))
                        }

                    },
                    onStopRecording = {

                        val result = pendingRecording ?: return@MyCameraBottomControls
                        pendingRecording = null
                        scope.launch {
                            onEvent(CameraEvent.OnRecordVideoSwitch(result))
                            val uri = File(context.filesDir, VideoRecordingFormat.FILE_NAME).toUri()
                            onGoToPreview(uri, MediaTypeToSend.VIDEO)
                        }
                    },
                    onFlipCamera = { onEvent(CameraEvent.OnFlipCamera) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.7f),
                )
            }

            ContentCreationMode.Audio -> {
                AudioBottomControls(
                    enabled = hasMicrophonePermission,
                    isRecording = cameraState.isRecording,
                    onStartRecording = { onEvent(CameraEvent.OnRecordAudioSwitch) },
                    onStopRecording = {
                        onEvent(CameraEvent.OnRecordAudioSwitch)
                        val uri = File(context.filesDir, AudioRecordingFormat.FILE_NAME).toUri()
                        onGoToPreview(uri, MediaTypeToSend.AUDIO)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.7f),
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1.3f)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            GalleryButton(modifier = Modifier, onClick = {})
        }
    }

}

