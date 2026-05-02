package com.project.momentum.features.contentcreation.media

import android.net.Uri
import androidx.camera.video.VideoRecordEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.project.momentum.features.contentcreation.state.CameraScreenState
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Stable
class CameraRecordingController(
    private val state: CameraScreenState,
    private val captureActions: CameraCaptureActions,
    private val scope: CoroutineScope,
    private val maxRecordMs: Int,
) {
    val progress = Animatable(0f)
    val captureButtonState = mutableStateOf(false)

    private var progressJob: Job? = null

    fun start(
        onSaved: (Uri, MediaTypeToSend) -> Unit
    ) {
        if (state.hasActiveRecording) return

        state.prepareRecording()
        var newRecording: androidx.camera.video.Recording? = null
        newRecording =
            captureActions.startRecording(state.videoCapture, onSaved = onSaved) { event ->
                when (event) {
                    is VideoRecordEvent.Start -> {
                        state.markRecordingStarted()
                        if (state.consumeStopRequest()) {
                            newRecording?.stop()
                        }
                    }

                    is VideoRecordEvent.Finalize -> {
                        if (!event.hasError()) {
                            onSaved(event.outputResults.outputUri, MediaTypeToSend.VIDEO)
                        }
                        state.clearRecording()
                    }

                    else -> Unit
                }
            }

        state.attachRecording(newRecording)
        launchProgress()
    }

    fun stop(resetProgress: Boolean = true) {
        stopRecording()
        stopProgress(resetProgress)
    }

    fun dispose() {
        stopRecording()
        progressJob?.cancel()
        progressJob = null
    }

    private fun launchProgress() {
        progressJob?.cancel()
        progressJob = scope.launch {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = maxRecordMs,
                    easing = LinearEasing,
                ),
            )
            stopRecording()
            progress.snapTo(0f)
            progressJob = null
        }
    }

    private fun stopProgress(reset: Boolean) {
        val activeJob = progressJob
        progressJob = null
        activeJob?.cancel()

        scope.launch {
            progress.stop()
            if (reset) {
                progress.snapTo(0f)
            }
        }
    }

    private fun stopRecording() {
        if (!state.hasActiveRecording) return

        if (state.recordingStarted) {
            captureActions.stopRecording(state.recording)
            state.detachRecording()
        } else {
            state.requestStopBeforeStart()
        }
    }
}

@Composable
fun rememberCameraRecordingController(
    state: CameraScreenState,
    captureActions: CameraCaptureActions,
    maxRecordMs: Int,
): CameraRecordingController {
    val scope = rememberCoroutineScope()
    return remember(state, captureActions, scope, maxRecordMs) {
        CameraRecordingController(
            state = state,
            captureActions = captureActions,
            scope = scope,
            maxRecordMs = maxRecordMs,
        )
    }
}
