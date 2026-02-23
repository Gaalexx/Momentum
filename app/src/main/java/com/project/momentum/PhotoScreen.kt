@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.text.SimpleDateFormat
import java.util.Locale

import android.Manifest
import android.R.attr.progress
import android.content.Context
import android.content.pm.PackageManager
import android.util.Rational
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.camera.core.Camera
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.MediaStoreOutputOptions
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import kotlin.math.min

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource


import androidx.camera.core.Preview as CameraXPreview


import androidx.camera.core.AspectRatio
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    torchEnabled: Boolean,
    imageCapture: ImageCapture,
    videoCapture: VideoCapture<Recorder>
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
    var camera by remember { mutableStateOf<Camera?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { previewView }
    )

    LaunchedEffect(lensFacing, imageCapture, videoCapture) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = CameraXPreview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val selector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()

                val rotation = previewView.display.rotation
                imageCapture.setTargetRotation(rotation)
                imageCapture.setCropAspectRatio(Rational(1, 1))

                val viewPort = ViewPort.Builder(Rational(1, 1), rotation)
                    .setScaleType(ViewPort.FILL_CENTER)
                    .build()

                val group = UseCaseGroup.Builder()
                    .setViewPort(viewPort)
                    .addUseCase(preview)
                    .addUseCase(imageCapture)
                    .addUseCase(videoCapture)
                    .build()


                val boundCamera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    group

                )
                camera = boundCamera

                if (boundCamera.cameraInfo.hasFlashUnit()) {
                    boundCamera.cameraControl.enableTorch(torchEnabled)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    LaunchedEffect(torchEnabled) {
        val cam = camera ?: return@LaunchedEffect
        if (cam.cameraInfo.hasFlashUnit()) {
            cam.cameraControl.enableTorch(torchEnabled)
        }
    }
}

private fun takePhoto(
    context: android.content.Context,
    imageCapture: ImageCapture,
    isFrontCamera: Boolean,
    onSaved: (android.net.Uri) -> Unit = {},
    onError: (Exception) -> Unit = {}
) {
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis())

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Momentum")
        }
    }

    val metadata = ImageCapture.Metadata().apply {
        isReversedHorizontal = isFrontCamera
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .setMetadata(metadata)
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                output.savedUri?.let(onSaved)
            }

            override fun onError(exc: ImageCaptureException) {
                onError(exc)
            }
        }
    )
}

private fun startVideoRecording(
    context: Context,
    videoCapture: VideoCapture<Recorder>,
    onEvent: (VideoRecordEvent) -> Unit = {}
): Recording {
    val hasAudioPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    val name = "MOMENTUM_VID_${System.currentTimeMillis()}.mp4"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Momentum")
        }
    }

    val mediaStoreOutput = MediaStoreOutputOptions.Builder(
        context.contentResolver,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    ).setContentValues(contentValues)
        .build()

    val pending =
        if (hasAudioPermission) videoCapture.output.prepareRecording(context, mediaStoreOutput)
            .withAudioEnabled() else videoCapture.output.prepareRecording(context, mediaStoreOutput)

    return pending.start(ContextCompat.getMainExecutor(context)) { event ->
        onEvent(event)
        if (event is VideoRecordEvent.Finalize) {
            if (event.hasError()) {
                Log.e(
                    "CameraLikeScreen",
                    "Video record error=${event.error}",
                    event.cause
                )
                Toast.makeText(
                    context,
                    videoRecordErrorMessage(context, event.error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.recorder_video_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

private fun videoRecordErrorMessage(context: Context, error: Int): String {
    return when (error) {
        VideoRecordEvent.Finalize.ERROR_NO_VALID_DATA -> {
            context.getString(
                R.string.recorder_video_error,
                context.getString(R.string.recorder_video_error_no_data)
            )
        }

        else -> context.getString(R.string.recorder_video_error, error.toString())
    }
}

private fun stopVideoRecording(
    recording: Recording?
): Recording? {
    recording?.stop()
    return null
}


@Composable
fun rememberCameraPermissionState(): State<Boolean> {
    val context = LocalContext.current
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
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


@Composable
fun CameraLikeScreen(
    previewPainter: Painter? = null,
    modifier: Modifier = Modifier,
    onGoToPreview: (android.net.Uri) -> Unit,
    onGoToRecorder: () -> Unit,
    onProfileClick: () -> Unit,
    onOpenGallery: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    maxRecordMs: Int = 10_000
) {
    val backGround = ConstColours.BLACK
    val iconTint = ConstColours.WHITE

    val context = LocalContext.current
    var torchEnabled by remember { mutableStateOf(false) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val hasCameraPermission by rememberCameraPermissionState()

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val videoCapture = remember {
        VideoCapture.withOutput(Recorder.Builder().build())
    }
    var recording by remember { mutableStateOf<Recording?>(null) }
    var recordingStarted by remember { mutableStateOf(false) }
    var stopRequested by remember { mutableStateOf(false) }

    // swipe state
    var dragOffset by remember { mutableStateOf(0f) }
    val swipeThreshold = 80f

    val swipeEnabled = recording == null

    val progress = remember { androidx.compose.animation.core.Animatable(0f) }
    val progressPath = remember { PathMeasure() }
    val scope = rememberCoroutineScope()

    val progressStarted = remember { mutableStateOf(false) }

    fun longPress() {
        if (recording == null) {
            var newRecording: Recording? = null
            recordingStarted = false
            stopRequested = false
            newRecording = startVideoRecording(
                context = context,
                videoCapture = videoCapture
            ) { event ->
                when (event) {
                    is VideoRecordEvent.Start -> {
                        recordingStarted = true
                        if (stopRequested) {
                            newRecording?.stop()
                            stopRequested = false
                        }
                    }

                    is VideoRecordEvent.Finalize -> {
                        recordingStarted = false
                        stopRequested = false
                        recording = null
                    }

                    else -> Unit
                }
            }
            recording = newRecording
        }
    }

    fun longPressEnd() {
        if (recording != null) {
            if (recordingStarted) {
                recording = stopVideoRecording(recording)
            } else {
                stopRequested = true
            }
        }
    }

    fun startProgress() {
        scope.launch {
            progress.snapTo(0f)
            progressStarted.value = true
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = maxRecordMs,
                    easing = LinearEasing
                )
            )
            longPressEnd()
            progressStarted.value = false
            progress.snapTo(0f)
        }
    }

    fun stopProgress(reset: Boolean = true) {
        scope.launch {
            progress.stop()
            if (reset) progress.snapTo(0f)
        }
    }





    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backGround)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(swipeEnabled) {
                if (!swipeEnabled) return@pointerInput
                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        dragOffset += dragAmount
                    },
                    onDragEnd = {
                        if (dragOffset < -swipeThreshold) onOpenGallery()
                        dragOffset = 0f
                    },
                    onDragCancel = { dragOffset = 0f }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileCircleButton(onClick = onProfileClick)

            Spacer(Modifier.weight(1f))
            FriendsPillButton(onClick = onGoToFriends)
            Spacer(Modifier.weight(1f))

            SettingsCircleButton(onClick = onGoToSettings)
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(60.dp))
                .background(ConstColours.BLACK)
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()

            ) {

                val fullPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                topLeft = Offset(0f, 0f),
                                bottomRight = Offset(size.width, size.height)
                            ),
                            cornerRadius = CornerRadius(x = 65.dp.toPx())
                        ),
                        direction = Path.Direction.Clockwise
                    )
                }

                progressPath.setPath(fullPath, forceClosed = true)
                val totalLength = progressPath.length
                val startShiftFraction = 0.3425f

                val start = totalLength * startShiftFraction
                val visibleLength = totalLength * progress.value
                val end = start + visibleLength

                val segmentPath = Path()

                if (end <= totalLength) {
                    progressPath.getSegment(
                        startDistance = start,
                        stopDistance = end,
                        destination = segmentPath,
                        startWithMoveTo = true
                    )
                } else {
                    progressPath.getSegment(
                        startDistance = start,
                        stopDistance = totalLength,
                        destination = segmentPath,
                        startWithMoveTo = true
                    )

                    val secondPart = Path()
                    progressPath.getSegment(
                        startDistance = 0f,
                        stopDistance = end - totalLength,
                        destination = secondPart,
                        startWithMoveTo = true
                    )

                    segmentPath.addPath(secondPart)
                }

                drawPath(
                    path = segmentPath,
                    color = ConstColours.GOLD,
                    style = Stroke(width = 8.dp.toPx())
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(ConstColours.MAIN_BACK_GRAY)
                    .align(Alignment.Center)
            ) {
                if (hasCameraPermission) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        lensFacing = lensFacing,
                        torchEnabled = torchEnabled,
                        imageCapture = imageCapture,
                        videoCapture = videoCapture
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.35f),
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }
            }

        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleButton(
                size = 60.dp,
                onClick = {},
                icon = Icons.Outlined.PhotoCamera,
                backgroundColor = ConstColours.BLACK
            )
            CircleButton(
                size = 60.dp,
                onClick = onGoToRecorder,
                icon = Icons.Outlined.Mic
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { torchEnabled = !torchEnabled },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        Icons.Outlined.WbSunny,
                        modifier = Modifier.size(40.dp),
                        contentDescription = stringResource(R.string.icon_flash),
                        tint = iconTint
                    )
                }

                Spacer(Modifier.weight(1f))

                BigCircleForMainScreenAction(
                    onClick = {
                        takePhoto(
                            context = context,
                            imageCapture = imageCapture,
                            isFrontCamera = (lensFacing == CameraSelector.LENS_FACING_FRONT),
                            onSaved = { uri ->
                                //Toast.makeText(context, "Saved: $uri", Toast.LENGTH_SHORT).show()
                                onGoToPreview(uri)
                            },
                            onError = {
//                                e ->
//                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onLongPressStart = {
                        longPress()
                    },
                    onLongPressEnd = {
                        longPressEnd()
                    },
                    onStartProgress = { startProgress() },
                    onEndProgress = { stopProgress() },
                    progressStarted = progressStarted
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {
                        lensFacing =
                            if (lensFacing == CameraSelector.LENS_FACING_BACK)
                                CameraSelector.LENS_FACING_FRONT
                            else
                                CameraSelector.LENS_FACING_BACK
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        Icons.Outlined.Cached,
                        modifier = Modifier.size(40.dp),
                        contentDescription = "Flip camera",
                        tint = iconTint
                    )
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        val progress = ((-dragOffset) / swipeThreshold).coerceIn(0f, 1f)

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowUp,
            contentDescription = "Swipe up",
            tint = iconTint.copy(alpha = 0.6f + 0.4f * progress),
            modifier = Modifier
                .size((34 + 8 * progress).dp)
                .offset(y = (-10 * progress).dp)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun CameraLikeScreenPreview() {
    MaterialTheme {
        CameraLikeScreen(
            onGoToPreview = {},
            onGoToRecorder = {},
            onProfileClick = {},
            onOpenGallery = {},
            onGoToSettings = {},
            onGoToFriends = {}
        )
    }
}
