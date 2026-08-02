package com.project.momentum.features.newcontentcreation.ui

import android.graphics.Bitmap
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import com.project.momentum.features.contentcreation.permissions.rememberCameraPermissionState
import com.project.momentum.features.contentcreation.permissions.rememberMicrophonePermissionState
import com.project.momentum.features.contentcreation.ui.DefaultMaxRecordMs
import com.project.momentum.features.newcontentcreation.ui.assets.AudioBottomControls
import com.project.momentum.features.newcontentcreation.ui.assets.GalleryButton
import com.project.momentum.features.newcontentcreation.ui.assets.MyCameraBottomControls
import com.project.momentum.features.newcontentcreation.ui.assets.MyMediaCreationModeSwitcher
import com.project.momentum.features.newcontentcreation.ui.assets.MyMediaCreationPreviewCard
import com.project.momentum.features.newcontentcreation.viewmodel.CameraEvent
import com.project.momentum.features.newcontentcreation.viewmodel.CameraState
import com.project.momentum.features.newcontentcreation.viewmodel.NewCameraViewModel
import com.project.momentum.ui.theme.ConstColours
import kotlinx.coroutines.launch

@Composable
fun MyMediaCreationRoot(
    modifier: Modifier = Modifier,
    onGoToPreview: (MediaTypeToSend) -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    contentCreationViewModel: NewCameraViewModel = hiltViewModel()
) {
    val vmState = contentCreationViewModel.state.collectAsStateWithLifecycle().value
    val controller = contentCreationViewModel.controller
    val onEvent = contentCreationViewModel::onEvent
    val lastImage = contentCreationViewModel.lastImage.collectAsStateWithLifecycle().value

    MyMediaCreationScreen(
        modifier = modifier,
        onGoToPreview = onGoToPreview,
        onProfileClick = onProfileClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
        cameraState = vmState,
        onEvent = onEvent,
        controller = controller,
        lastImage = lastImage
    )
}

@Composable
fun MyMediaCreationScreen(
    modifier: Modifier = Modifier,
    onGoToPreview: (MediaTypeToSend) -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    cameraState: CameraState,
    onEvent: (CameraEvent) -> Unit,
    controller: LifecycleCameraController,
    lastImage: Bitmap?
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

    if (cameraState.isRecording) {
        LaunchedEffect(Unit) {
            scope.launch {
                progress.snapTo(0f)
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = DefaultMaxRecordMs,
                        easing = LinearEasing,
                    ),
                )
                onEvent(CameraEvent.OnStopAllRecords)
                progress.snapTo(0f)
            }
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
                MyCameraBottomControls(
                    torchEnabled = cameraState.torchEnabled,
                    captureEnabled = hasCameraPermission,
                    captureButtonState = cameraState.isRecording,//cameraCaptureButtonState,
                    onToggleTorch = { onEvent(CameraEvent.OnToggleFlash) },
                    onTakePhoto = {
                        onEvent(CameraEvent.OnTakePhoto)
                        if (lastImage == null) {
                            Toast.makeText(
                                context,
                                "Error with camera",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            onGoToPreview(MediaTypeToSend.PHOTO)
                        }
                    },
                    onStartRecording = {
                        onEvent(CameraEvent.OnRecordVideoSwitch)
                    },
                    onStopRecording = {
                        onEvent(CameraEvent.OnRecordVideoSwitch)
                        onGoToPreview(MediaTypeToSend.VIDEO)
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
                        onGoToPreview(MediaTypeToSend.AUDIO)
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
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            GalleryButton(modifier = Modifier, onClick = {})
        }
    }

}

