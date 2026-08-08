package com.project.momentum.features.contentcreation.ui

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.camera.core.FocusMeteringAction
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CameraView(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var focus by remember { mutableStateOf<Offset?>(null) }

    val progress = remember { Animatable(0f) }

    val previewView = remember {
        PreviewView(context).apply {
            this.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
            controller.isTapToFocusEnabled = false
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    }

    LaunchedEffect(focus) {
        if (focus != null) {
            progress.snapTo(0f)

            progress.animateTo(
                1f,
                animationSpec = spring()
            )
            delay(600.milliseconds)
            focus = null
        }
    }

    Box(modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView },
        )

        Canvas(
            Modifier
                .fillMaxSize()
                .pointerInput(previewView) {
                    detectTransformGestures { _, _, zoom, _ ->
                        if (zoom != 1f) {
                            val state = controller.zoomState.value
                            val current = state?.zoomRatio ?: 1f
                            val min = state?.minZoomRatio ?: 1f
                            val max = state?.maxZoomRatio ?: 1f
                            controller.setZoomRatio((current * zoom).coerceIn(min, max))
                        }
                    }
                }
                .pointerInput(previewView) {
                    detectTapGestures { offset ->
                        focus = offset
                        val point = previewView.meteringPointFactory
                            .createPoint(offset.x, offset.y)
                        runCatching {
                            controller.cameraControl?.startFocusAndMetering(
                                FocusMeteringAction.Builder(point).build()
                            )
                        }
                    }
                }
        ) {
            focus?.let {
                drawCircle(
                    Color.White,
                    progress.value * 15.dp.toPx(),
                    it,
                    style = Stroke(2.dp.toPx())
                )
            }
        }
    }
}