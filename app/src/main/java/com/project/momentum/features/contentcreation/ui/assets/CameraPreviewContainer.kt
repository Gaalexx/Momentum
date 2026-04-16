package com.project.momentum.features.contentcreation.ui.assets

import android.util.Rational
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.project.momentum.features.contentcreation.state.CameraScreenState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Composable
fun CameraPreviewContainer(
    state: CameraScreenState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val cameraProviderFuture = remember(context) {
        ProcessCameraProvider.getInstance(context)
    }
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    var camera by remember { mutableStateOf<Camera?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { previewView },
    )

    LaunchedEffect(state.lensFacing, state.imageCapture, state.videoCapture) {
        val cameraProvider = cameraProviderFuture.await(mainExecutor)
        camera = bindCameraUseCases(
            cameraProvider = cameraProvider,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            lensFacing = state.lensFacing,
            imageCapture = state.imageCapture,
            videoCapture = state.videoCapture,
        )
        updateTorch(camera = camera, torchEnabled = state.torchEnabled)
    }

    LaunchedEffect(camera, state.torchEnabled) {
        updateTorch(camera = camera, torchEnabled = state.torchEnabled)
    }

    DisposableEffect(cameraProviderFuture) {
        onDispose {
            if (cameraProviderFuture.isDone) {
                cameraProviderFuture.get().unbindAll()
            }
        }
    }
}

private fun bindCameraUseCases(
    cameraProvider: ProcessCameraProvider,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    lensFacing: Int,
    imageCapture: ImageCapture,
    videoCapture: VideoCapture<Recorder>,
): Camera {
    val preview = CameraXPreview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }

    val selector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    cameraProvider.unbindAll()

    val rotation = previewView.display?.rotation ?: Surface.ROTATION_0
    imageCapture.targetRotation = rotation
    videoCapture.targetRotation = rotation
    imageCapture.setCropAspectRatio(Rational(1, 1))

    val viewPort = ViewPort.Builder(Rational(1, 1), rotation)
        .setScaleType(ViewPort.FILL_CENTER)
        .build()

    val useCaseGroup = UseCaseGroup.Builder()
        .setViewPort(viewPort)
        .addUseCase(preview)
        .addUseCase(imageCapture)
        .addUseCase(videoCapture)
        .build()

    return cameraProvider.bindToLifecycle(
        lifecycleOwner,
        selector,
        useCaseGroup,
    )
}

private fun updateTorch(
    camera: Camera?,
    torchEnabled: Boolean,
) {
    val activeCamera = camera ?: return
    if (activeCamera.cameraInfo.hasFlashUnit()) {
        activeCamera.cameraControl.enableTorch(torchEnabled)
    }
}

private suspend fun <T> ListenableFuture<T>.await(executor: java.util.concurrent.Executor): T =
    suspendCancellableCoroutine { continuation ->
        addListener(
            {
                try {
                    continuation.resume(get())
                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            },
            executor,
        )
    }
