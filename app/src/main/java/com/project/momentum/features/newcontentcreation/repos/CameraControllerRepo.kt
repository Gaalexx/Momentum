package com.project.momentum.features.newcontentcreation.repos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import com.project.momentum.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject


@ViewModelScoped
class CameraControllerRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    var _videoRecording: Recording? = null
        private set

    var _controller: LifecycleCameraController = LifecycleCameraController(context).apply {
        setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }
        private set

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val lensFacing = _lensFacing.asStateFlow()

    private val _torchEnabled = MutableStateFlow(false)
    val torchEnabled = _torchEnabled.asStateFlow()

    fun flipCamera() {
        if (_videoRecording != null) {
            return
        }

        val newLens =
            if (_lensFacing.value == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }

        val selector = CameraSelector.Builder()
            .requireLensFacing(newLens)
            .build()

        if (_controller.hasCamera(selector)) {
            _controller.cameraSelector = selector
            _lensFacing.update {
                newLens
            }
        }
    }

    private fun hasTorch(): Boolean =
        _controller.cameraInfo?.hasFlashUnit() == true

    fun toggleTorch(): Boolean {
        val newTorchState = !torchEnabled.value

        if (_controller.torchState.isInitialized && hasTorch()) {
            _controller.enableTorch(newTorchState)
            _torchEnabled.update { newTorchState }
            return true
        }
        return false
    }

    fun onTakePhoto(): Bitmap? {
        var result: Bitmap? = null
        _controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val rect = image.cropRect
                    val degrees = image.imageInfo.rotationDegrees.toFloat()
                    val full = image.toBitmap()
                    image.close()
                    val matrix = android.graphics.Matrix()
                    matrix.postRotate(degrees)
                    result = Bitmap.createBitmap(
                        full, rect.left, rect.top, rect.width(), rect.height(), matrix, true
                    )
                    // _state.update { it.copy(lastImage = result) } // TODO не забыть перенести во вью модель
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("PHOTO CAMERA", "Photo error occurred")
                    // Toast.makeText(context, "Photo error occurred", Toast.LENGTH_LONG).show()
                }

            })
        return result
    }

    @SuppressLint("MissingPermission")
    fun onRecordVideo(): Boolean {
        if (_videoRecording != null) {
            _videoRecording!!.stop()
//            _state.update {                   // TODO не забыть перенести во вью модель
//                it.copy(isRecording = false)
//            }
            return true
        }

        val output = File(context.filesDir, "myRec.mp4")

//        _state.update {                       // TODO не забыть перенести во вью модель
//            it.copy(isRecording = true)
//        }
        var result: Boolean = false
        _videoRecording = _controller.startRecording(
            FileOutputOptions.Builder(output).build(),
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(context),
        ) { event ->
            when (event) {
                is VideoRecordEvent.Finalize -> {
                    if (event.hasError()) {
                        _videoRecording?.close()
                        _videoRecording = null
                        result = false
                        Log.e("VIDEO RECORDER", "Video recording failed")
                    } else {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun recordingIsActive(): Boolean {
        return _videoRecording != null
    }


}