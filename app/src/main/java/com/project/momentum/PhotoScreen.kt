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
import androidx.compose.ui.Alignment
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
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.text.SimpleDateFormat
import java.util.Locale

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import kotlin.math.min


import androidx.camera.core.Preview as CameraXPreview




@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    torchEnabled: Boolean,
    imageCapture: ImageCapture
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var camera by remember { mutableStateOf<Camera?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

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
                    val boundCamera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageCapture
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
    )

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
    onOpenGallery: () -> Unit
    onGoToSettings: () -> Unit
) {
    val backGround = ConstColours.BLACK
    val mainBackGray = ConstColours.MAIN_BACK_GRAY
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

    // Для свайпа
    var dragOffset by remember { mutableStateOf(0f) }
    val swipeThreshold = 50f // Порог свайпа

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backGround)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        // Свайп вверх (отрицательный Y)
                        val verticalDrag = dragAmount.y
                        if (verticalDrag < -50) { // Начинаем отслеживать только при значительном движении вверх
                            dragOffset = -verticalDrag // Делаем положительным для удобства
                        }
                    },
                    onDragEnd = {
                        // Проверяем, достаточно ли сильный свайп
                        if (dragOffset > swipeThreshold) {
                            onOpenGallery()
                        }
                        dragOffset = 0f
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileCircleButton(
                    onClick = onProfileClick
                )

                Spacer(Modifier.weight(1f))
                FriendsPillButton(onClick = {})
                Spacer(Modifier.weight(1f))

                SettingsCircleButton(onClick = {})
            }

            Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .aspectRatio(1.10f)
                .clip(RoundedCornerShape(28.dp))
                .background(ConstColours.MAIN_BACK_GRAY
)
        ) {
            if (hasCameraPermission) {
//                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Icon(
//                        imageVector = Icons.Outlined.PhotoCamera,
//                        contentDescription = null,
//                        tint = Color.White.copy(alpha = 0.35f),
//                        modifier = Modifier.size(56.dp)
//                    )
//                }
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    lensFacing = lensFacing,
                    torchEnabled = torchEnabled,
                    imageCapture = imageCapture
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.35f),
                        modifier = Modifier.size(56.dp)
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
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { torchEnabled = !torchEnabled }, modifier = Modifier.size(50.dp)) {
                        Icon(Icons.Outlined.WbSunny, modifier = Modifier.size(40.dp), contentDescription = "Flash", tint = iconTint)

                    }

                Spacer(Modifier.weight(1f))
                BigCircleForMainScreenAction(onClick = {

                    takePhoto(
                        context = context,
                        imageCapture = imageCapture,
                        isFrontCamera = (lensFacing == CameraSelector.LENS_FACING_FRONT),
                        onSaved = { uri ->
                            Toast.makeText(context, "Saved: $uri", Toast.LENGTH_SHORT).show()
                            onGoToPreview(uri)
                        },
                        onError = { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    )

                })

            }

            Spacer(Modifier.height(15.dp))

            // Просто иконка без интерактивности
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Анимация иконки при свайпе
                    val progress = minOf(dragOffset / swipeThreshold, 1f)

                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = "Свайп вверх для галереи",
                        tint = iconTint.copy(alpha = 0.7f + progress * 0.3f),
                        modifier = Modifier
                            .size((34 + progress * 10).dp)
                            .offset(y = (-progress * 20).dp)
                    )

                    Text(
                        text = "Свайп вверх для галереи",
                        color = iconTint.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun PreviewPillIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    backGround
: Color,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = backGround
.copy(alpha = 0.9f),
        contentColor = tint,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun CameraLikeScreenPreview() {
    MaterialTheme {
        CameraLikeScreen(onGoToPreview = {  }, previewPainter = null, onGoToRecorder = {}, onProfileClick = {}, onGoToSettings = {})
    }
}
