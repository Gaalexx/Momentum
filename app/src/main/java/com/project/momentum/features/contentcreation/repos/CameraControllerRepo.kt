package com.project.momentum.features.contentcreation.repos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.MirrorMode
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.project.momentum.di.IoDispatcher
import com.project.momentum.features.contentcreation.mediaconfig.PhotoRecordingFormat
import com.project.momentum.features.contentcreation.mediaconfig.VideoRecordingFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.resumeWithException


@ViewModelScoped
class CameraControllerRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val io: CoroutineDispatcher
) {

    private companion object {
        const val TAG = "CAMERA REPOSITORY"
    }

    var _videoRecording: Recording? = null
        private set

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_FRONT)
    val lensFacing = _lensFacing.asStateFlow()

    private val _torchEnabled = MutableStateFlow(false)
    val torchEnabled = _torchEnabled.asStateFlow()

    var _controller: LifecycleCameraController = LifecycleCameraController(context).apply {
        setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(_lensFacing.value)
            .build()
        videoCaptureMirrorMode = MirrorMode.MIRROR_MODE_ON_FRONT_ONLY
        isTapToFocusEnabled = false
    }
        private set


    fun flipCamera() {
        if (_videoRecording != null) {
            Log.e(TAG, "Flip camera error!")
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

        if (hasTorch()) {
            _controller.enableTorch(newTorchState)
            _torchEnabled.update { newTorchState }
            return true
        }
        return false
    }

    suspend fun onTakePhoto(): Uri {
        val output = File(context.filesDir, PhotoRecordingFormat.FILE_NAME)
        val result: Bitmap = takePicture()
        withContext(Dispatchers.IO) {
            FileOutputStream(output).use { out ->
                result.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        }
        result.recycle()
        return output.toUri()
    }


    private suspend fun takePicture(): Bitmap = suspendCancellableCoroutine { cont ->
        _controller.takePicture(
            context.mainExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    try {
                        val rect = image.cropRect
                        val degrees = image.imageInfo.rotationDegrees.toFloat()
                        val full = image.toBitmap()
                        val matrix = android.graphics.Matrix().apply {
                            if (_lensFacing.value == CameraSelector.LENS_FACING_FRONT) {
                                preScale(1f, -1f)
                            }
                        }
                        matrix.postRotate(degrees)
                        val result = Bitmap.createBitmap(
                            full, rect.left, rect.top, rect.width(), rect.height(), matrix, true
                        )
                        if (result != full) {
                            full.recycle()
                        }
                        cont.resume(result) { _, b, _ -> b.recycle() }
                    } catch (e: Exception) {
                        cont.resumeWithException(e)
                    } finally {
                        image.close()
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(TAG, "Photo error occurred")
                }

            }
        )
    }

    @SuppressLint("MissingPermission")
    suspend fun onRecordVideo(result: CompletableDeferred<Uri?>): Uri? {
        if (_videoRecording != null) {
            _videoRecording!!.stop()
            _videoRecording = null
            return result.await()
        }

        val output = File(context.filesDir, VideoRecordingFormat.FILE_NAME)

        try {
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
                            Log.e(TAG, "Video recording failed")

                            result.complete(null)
                        } else {
                            result.complete(event.outputResults.outputUri)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            result.complete(null)
        }
        return null
    }

    fun recordingIsActive(): Boolean {
        return _videoRecording != null
    }


}