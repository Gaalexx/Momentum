package com.project.momentum.features.contentcreation.state

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.MirrorMode
import androidx.camera.core.Preview
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class CameraScreenState(
    val preview: Preview,
    val imageCapture: ImageCapture,
    val videoCapture: VideoCapture<Recorder>,
    val cameraLifecycleOwner: CameraLifecycleOwner,
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


    fun startCameraSession() {
        cameraLifecycleOwner.start()
    }

    fun pauseCameraSession() {
        cameraLifecycleOwner.pause()
    }

    fun destroyCameraSession() {
        cameraLifecycleOwner.destroy()
    }

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

    val preview = remember {
        Preview.Builder().build()
    }

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

    val cameraLifecycleOwner = remember {
        CameraLifecycleOwner()
    }

    DisposableEffect(cameraLifecycleOwner) {
        onDispose {
            cameraLifecycleOwner.destroy()
        }
    }

    return remember(preview, imageCapture, videoCapture, cameraLifecycleOwner, initialLensFacing) {
        CameraScreenState(
            preview = preview,
            imageCapture = imageCapture,
            videoCapture = videoCapture,
            cameraLifecycleOwner = cameraLifecycleOwner,
            initialLensFacing = initialLensFacing,
        )
    }
}
