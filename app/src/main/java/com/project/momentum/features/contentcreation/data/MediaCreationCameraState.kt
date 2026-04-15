package com.project.momentum.features.contentcreation.data

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.MirrorMode
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


enum class MediaTypeToSend(val enumerate: Int) {
    PHOTO(0),
    VIDEO(1),
    AUDIO(2)
}

@Stable
class CameraScreenState(
    val imageCapture: ImageCapture,
    val videoCapture: VideoCapture<Recorder>,
    initialLensFacing: Int,
) {
    var torchEnabled by mutableStateOf(false)
        private set

    var lensFacing by mutableIntStateOf(initialLensFacing)
        private set

    var recording by mutableStateOf<Recording?>(null)
        private set

    var recordingStarted by mutableStateOf(false)
        private set

    var stopRequested by mutableStateOf(false)
        private set

    val isFrontCamera: Boolean
        get() = lensFacing == CameraSelector.LENS_FACING_FRONT

    val hasActiveRecording: Boolean
        get() = recording != null

    fun toggleTorch() {
        torchEnabled = !torchEnabled
    }

    fun flipCamera() {
        lensFacing = if (isFrontCamera) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
    }

    fun prepareRecording() {
        recordingStarted = false
        stopRequested = false
    }

    fun attachRecording(recording: Recording?) {
        this.recording = recording
    }

    fun markRecordingStarted() {
        recordingStarted = true
    }

    fun requestStopBeforeStart() {
        stopRequested = true
    }

    fun consumeStopRequest(): Boolean {
        val shouldStop = stopRequested
        stopRequested = false
        return shouldStop
    }

    fun detachRecording() {
        recording = null
    }

    fun clearRecording() {
        recordingStarted = false
        stopRequested = false
        recording = null
    }
}

@Composable
fun rememberCameraScreenState(
    initialLensFacing: Int = CameraSelector.LENS_FACING_BACK,
): CameraScreenState {
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val videoCapture = remember {
        VideoCapture.Builder(Recorder.Builder().build())
            .setMirrorMode(MirrorMode.MIRROR_MODE_ON_FRONT_ONLY)
            .build()
    }

    return remember(imageCapture, videoCapture, initialLensFacing) {
        CameraScreenState(
            imageCapture = imageCapture,
            videoCapture = videoCapture,
            initialLensFacing = initialLensFacing,
        )
    }
}

@Composable
fun rememberCameraPermissionState(): State<Boolean> {
    val context = LocalContext.current
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED,
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasPermission.value = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission.value) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    return hasPermission
}
