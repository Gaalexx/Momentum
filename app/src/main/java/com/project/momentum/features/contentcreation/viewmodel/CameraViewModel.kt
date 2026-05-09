package com.project.momentum.features.contentcreation.viewmodel

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.MirrorMode
import androidx.camera.core.Preview
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.lifecycle.ViewModel
import com.project.momentum.features.contentcreation.state.CameraLifecycleOwner
import com.project.momentum.features.contentcreation.state.CameraScreenState
import com.project.momentum.features.contentcreation.state.rememberCameraScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<CameraScreenState>(
        CameraScreenState(
            preview = buildPreview(),
            imageCapture = buildImageCapture(),
            videoCapture = buildVideoCapture(),
            cameraLifecycleOwner = CameraLifecycleOwner(),
            initialLensFacing = CameraSelector.LENS_FACING_BACK,
        )
    )

    val state = _state.asStateFlow()

    private fun buildPreview(): Preview = Preview.Builder().build()
    private fun buildImageCapture(): ImageCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    private fun buildVideoCapture(): VideoCapture<Recorder> =
        VideoCapture.Builder(Recorder.Builder().build())
            .setMirrorMode(MirrorMode.MIRROR_MODE_ON_FRONT_ONLY)
            .build()
    
    override fun onCleared() {
        state.value.destroyCameraSession()
        super.onCleared()
    }

}