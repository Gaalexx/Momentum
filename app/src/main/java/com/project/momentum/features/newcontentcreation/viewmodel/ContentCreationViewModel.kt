package com.project.momentum.features.newcontentcreation.viewmodel


import android.content.Context
import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.newcontentcreation.repos.AudioRecorderRepo
import com.project.momentum.features.newcontentcreation.repos.CameraControllerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
data class CameraState(
    val torchEnabled: Boolean = false,
    val contentCreationMode: ContentCreationMode = ContentCreationMode.Camera,
    val isRecording: Boolean = false
)


sealed interface CameraEvent {
    data class OnTakePhoto(val result: CompletableDeferred<Uri>) : CameraEvent
    data object OnRecordVideoSwitch : CameraEvent

    data object OnRecordAudioSwitch : CameraEvent
    data object OnStopAllRecords : CameraEvent
    data class OnContentCreationModeChange(val mode: ContentCreationMode) : CameraEvent
    data object OnFlipCamera : CameraEvent
    data object OnToggleFlash : CameraEvent
}

@HiltViewModel
class NewCameraViewModel @Inject constructor(
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
                onRecordVideo()
            }

            is CameraEvent.OnContentCreationModeChange -> {
                onChangeMode(event)
            }

            is CameraEvent.OnRecordAudioSwitch -> {
                onRecorderSwitch()
            }

            is CameraEvent.OnStopAllRecords -> {
                onStopAllRecords()
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

    private fun onRecordVideo() {
        cameraRepo.onRecordVideo()
        _state.update {
            it.copy(
                isRecording = !it.isRecording
            )
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

    private fun onStopAllRecords() {
        if (cameraRepo.recordingIsActive()) {
            cameraRepo.onRecordVideo()
        }
        if (audioRepo.recordingIsActive()) {
            audioRepo.onRecorderSwitch()
        }
        _state.update {
            it.copy(
                isRecording = !it.isRecording
            )
        }
    }


}