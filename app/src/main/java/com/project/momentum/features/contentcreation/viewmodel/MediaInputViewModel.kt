package com.project.momentum.features.contentcreation.viewmodel


import android.content.Context
import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Stable
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.mediaconfig.AudioRecordingFormat
import com.project.momentum.features.contentcreation.repos.AudioRecorderRepo
import com.project.momentum.features.contentcreation.repos.CameraControllerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class CameraState(
    val torchEnabled: Boolean = false,
    val contentCreationMode: ContentCreationMode = ContentCreationMode.Camera,
    val isRecording: Boolean = false
)


sealed interface CameraEvent {
    data class OnTakePhoto(val result: CompletableDeferred<Uri>) : CameraEvent
    data class OnRecordVideoSwitch(val result: CompletableDeferred<Uri?>) : CameraEvent

    data object OnRecordAudioSwitch : CameraEvent
    data class OnStopAllRecords(val result: CompletableDeferred<Uri?>) : CameraEvent
    data class OnContentCreationModeChange(val mode: ContentCreationMode) : CameraEvent
    data object OnFlipCamera : CameraEvent
    data object OnToggleFlash : CameraEvent
}

@HiltViewModel
class MediaInputViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cameraRepo: CameraControllerRepo,
    private val audioRepo: AudioRecorderRepo
) : ViewModel() {

    private val _state = MutableStateFlow<CameraState>(CameraState())


    val controller: LifecycleCameraController get() = cameraRepo._controller

    val state = _state.asStateFlow()


    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnTakePhoto -> {
                onTakePhoto(event.result)
            }

            is CameraEvent.OnRecordVideoSwitch -> {
                onRecordVideo(event.result)
            }

            is CameraEvent.OnContentCreationModeChange -> {
                onChangeMode(event)
            }

            is CameraEvent.OnRecordAudioSwitch -> {
                onRecorderSwitch()
            }

            is CameraEvent.OnStopAllRecords -> {
                onStopAllRecords(event.result)
            }

            is CameraEvent.OnFlipCamera -> {
                onFlipCamera()
            }

            is CameraEvent.OnToggleFlash -> {
                onToggleFlash()
            }
        }
    }

    private fun onFlipCamera() {
        cameraRepo.flipCamera()
    }

    private fun onRecordVideo(result: CompletableDeferred<Uri?>) {
        viewModelScope.launch {
            val uri = cameraRepo.onRecordVideo(result)
            _state.update {
                it.copy(
                    isRecording = !it.isRecording
                )
            }
        }
    }

    private fun onToggleFlash() {
        if (cameraRepo.toggleTorch()) {
            _state.update {
                it.copy(torchEnabled = !it.torchEnabled)
            }
        }
    }

    private fun onTakePhoto(result: CompletableDeferred<Uri>) {
        viewModelScope.launch {
            val uri = cameraRepo.onTakePhoto()
            result.complete(uri)
        }
    }

    private fun onChangeMode(event: CameraEvent.OnContentCreationModeChange) {
        _state.update {
            it.copy(contentCreationMode = event.mode)
        }
    }

    private fun onRecorderSwitch() {
        audioRepo.onRecorderSwitch()
        _state.update {
            it.copy(
                isRecording = !it.isRecording
            )
        }
    }

    private fun onStopAllRecords(result: CompletableDeferred<Uri?>) {
        viewModelScope.launch {
            var uri: Uri? = null
            if (cameraRepo.recordingIsActive()) {
                cameraRepo.onRecordVideo(result) // result.complete внутри метода
            } else if (audioRepo.recordingIsActive()) {
                audioRepo.onRecorderSwitch()
                uri = File(context.filesDir, AudioRecordingFormat.FILE_NAME).toUri()
                result.complete(uri)
            }
            _state.update {
                it.copy(
                    isRecording = !it.isRecording
                )
            }
        }
    }


}