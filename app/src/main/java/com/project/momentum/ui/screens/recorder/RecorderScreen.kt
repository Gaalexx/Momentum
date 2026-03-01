package com.project.momentum.ui.screens.recorder

import android.content.ContentValues
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.momentum.ConstColours
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

import android.media.MediaRecorder
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import android.Manifest
import android.content.Context
import android.net.Uri

import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import com.project.momentum.R
import com.project.momentum.ui.assets.BigCircleMicroButton
import com.project.momentum.ui.assets.BigCircleSendPhotoAction
import com.project.momentum.ui.assets.CaptionBasicInput
import com.project.momentum.ui.assets.CircleButton
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton


@Composable
fun rememberMicrophonePermissionState(): State<Boolean> {
    val context = LocalContext.current
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
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
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    return hasPermission
}

fun startRecording(context: Context, mainState: MainState) {
    val fileName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
        .format(System.currentTimeMillis()) + ".3gp"

    val audioFile = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

    try {
        val recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(audioFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            prepare()
            start()
        }

        mainState.mediaRecorder = recorder
        mainState.audioFile = audioFile
        mainState.isRecording = true

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun stopRecording(mainState: MainState) {
    try {
        mainState.mediaRecorder?.apply {
            if (mainState.isRecording) {
                try {
                    stop()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                release()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        mainState.mediaRecorder = null
        mainState.isRecording = false
    }
}

fun saveToMediaStore(context: Context, audioFile: File): Uri? {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, audioFile.name)
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/3gpp")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/Momentum")
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        try {
            resolver.openOutputStream(it)?.use { outputStream ->
                audioFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            resolver.delete(uri, null, null)
            return null
        }
    }

    return uri
}

@Composable
fun RecordingProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 8f,
    color: Color = ConstColours.WHITE
) {
    Canvas(modifier = modifier) {
        val fullPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        topLeft = Offset(0f, 0f),
                        bottomRight = Offset(size.width, size.height)
                    ),
                    cornerRadius = CornerRadius(x = 60.dp.toPx())
                ),
                direction = Path.Direction.Clockwise
            )
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(fullPath, forceClosed = true)
        val totalLength = pathMeasure.length
        val startShiftFraction = 0.3425f

        val start = totalLength * startShiftFraction
        val visibleLength = totalLength * progress
        val end = start + visibleLength

        val segmentPath = Path()

        if (end <= totalLength) {
            pathMeasure.getSegment(
                startDistance = start,
                stopDistance = end,
                destination = segmentPath,
                startWithMoveTo = true
            )
        } else {
            pathMeasure.getSegment(
                startDistance = start,
                stopDistance = totalLength,
                destination = segmentPath,
                startWithMoveTo = true
            )

            val secondPart = Path()
            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = end - totalLength,
                destination = secondPart,
                startWithMoveTo = true
            )

            segmentPath.addPath(secondPart)
        }

        drawPath(
            path = segmentPath,
            color = color,
            style = Stroke(width = strokeWidth.dp.toPx())
        )
    }
}

@Composable
fun RecorderScreen(
    onCameraClick: () -> Unit,              // Для переключения на камеру
    onGoToFriends: () -> Unit,              // Для перехода к друзьям
    onProfileClick: () -> Unit,              // Для перехода в профиль
    onGoToSettings: () -> Unit,              // Для перехода в настройки
    modifier: Modifier = Modifier
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val mainState = remember { MainState() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(Unit) {
                // Здесь можно добавить обработку жестов
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя панель с кнопками
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileCircleButton(
                onClick = onProfileClick,  // Используем колбэк
                backgroundColor = chrome2
            )

            Spacer(Modifier.weight(1f))

            FriendsPillButton(onClick = onGoToFriends)  // Используем колбэк

            Spacer(Modifier.weight(1f))

            SettingsCircleButton(
                onClick = onGoToSettings,  // Используем колбэк
                backgroundColor = chrome2
            )
        }

        Spacer(Modifier.height(12.dp))

        // Основное изображение
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(60.dp))
                .background(ConstColours.BLACK)
        ) {
            if (mainState.currentState == "STATE_1" && mainState.isRecording) {
                RecordingProgressRing(
                    progress = mainState.recordingProgress,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color(0xFF2A2E39))
                    .align(Alignment.Center)
            ) {
                AsyncImage(
                    model = stringResource(R.string.rec_img_model_),
                    contentDescription = stringResource(R.string.recorder_main_image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (mainState.currentState == "STATE_2") {
                    val captionFocusRequester = remember { FocusRequester() }
                    var caption by remember { mutableStateOf("") }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        CaptionBasicInput(
                            value = caption,
                            onValueChange = { caption = it },
                            placeholder = stringResource(R.string.label_write_comment),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(captionFocusRequester)
                        )
                    }

                    LaunchedEffect(Unit) {
                        mainState.captionFocusRequester = captionFocusRequester
                    }
                }
            }
        }


        Spacer(Modifier.height(16.dp))

        if (mainState.currentState != "STATE_2") {
            Row(
                modifier = Modifier.padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleButton(
                    size = 60.dp,
                    onClick = onCameraClick,  // Используем колбэк для переключения на камеру
                    icon = Icons.Outlined.PhotoCamera,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )

                CircleButton(
                    size = 60.dp,
                    onClick = {},  // Микрофон уже активен, ничего не делаем
                    icon = Icons.Outlined.Mic,
                    backgroundColor = ConstColours.BLACK,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Нижняя секция с микрофоном и состояниями
        SecondaryImagesSection(
            mainState = mainState
        )
    }
}



class MainState {
    var currentState by mutableStateOf("INITIAL")
    var captionFocusRequester: FocusRequester? by mutableStateOf(null)

    var mediaRecorder: MediaRecorder? by mutableStateOf(null)
    var audioFile: File? by mutableStateOf(null)
    var isRecording by mutableStateOf(false)

    var recordingProgress by mutableStateOf(0f)
    val maxRecordMs: Int = 10_000
}

@Composable
fun SecondaryImagesSection(
    mainState: MainState
) {
    var isImage1Tinted by remember { mutableStateOf(false) }
    var isImage2Visible by remember { mutableStateOf(true) }
    var isLongPressActive by remember { mutableStateOf(false) }
    var recordingStarted by remember { mutableStateOf(false) }
    var stopRequested by remember { mutableStateOf(false) }

    val captionFocusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    var progressJob by remember { mutableStateOf<Job?>(null) }

    val iconTint = Color(0xFFEDEEF2)
    var showKeyboardTrigger by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current
    val hasMicPermission by rememberMicrophonePermissionState()

    LaunchedEffect(showKeyboardTrigger) {
        if (showKeyboardTrigger) {
            delay(100)

            val focusReq = mainState.captionFocusRequester
            if (focusReq != null) {
                repeat(3) { attempt ->
                    focusReq.requestFocus()
                    delay(50L * (attempt + 1))
                    keyboardController?.show()
                }
            } else {
                keyboardController?.show()
            }

            showKeyboardTrigger = false
        }
    }

    LaunchedEffect(Unit) {
        mainState.captionFocusRequester = captionFocusRequester
    }

    LaunchedEffect(isImage1Tinted, isLongPressActive) {
        mainState.currentState = when {
            !isImage1Tinted && !isLongPressActive -> "INITIAL"
            isImage1Tinted && isLongPressActive -> "STATE_1"
            isImage1Tinted && !isLongPressActive -> "STATE_2"
            else -> "INITIAL"
        }

        if (mainState.currentState == "STATE_2") {
            delay(100)
            captionFocusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopRecording(mainState)
            mainState.audioFile?.delete()
            progressJob?.cancel()
        }
    }

    fun resetToInitialState() {
        stopRecording(mainState)
        isImage1Tinted = false
        isImage2Visible = true
        isLongPressActive = false
        recordingStarted = false
        stopRequested = false
        progressJob?.cancel()
        progressJob = null
        mainState.recordingProgress = 0f
        keyboardController?.hide()
    }

    fun longPressEnd() {
        if (isImage1Tinted) {
            if (mainState.isRecording) {
                stopRecording(mainState)
                isLongPressActive = false
            } else {
                stopRequested = true
            }
            progressJob?.cancel()
            mainState.recordingProgress = 0f
        }
    }

    fun startProgressAnimation() {
        progressJob?.cancel()
        progressJob = scope.launch {
            val startTime = System.currentTimeMillis()
            while (isActive && mainState.isRecording) {
                val elapsed = System.currentTimeMillis() - startTime
                mainState.recordingProgress = (elapsed.toFloat() / mainState.maxRecordMs).coerceIn(0f, 1f)

                if (elapsed >= mainState.maxRecordMs) {
                    longPressEnd()
                    break
                }
                delay(16)
            }
        }
    }

    fun longPressStart() {
        if (!hasMicPermission) return

        if (!isImage1Tinted) {
            recordingStarted = false
            stopRequested = false

            isImage1Tinted = true
            isLongPressActive = true

            startRecording(context, mainState)
            startProgressAnimation()
        }
    }




    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 23.dp)
    ) {
        if (mainState.currentState == "STATE_2") {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { resetToInitialState() },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                modifier = Modifier.size(40.dp),
                                contentDescription = stringResource(R.string.recorder_reset_content_description),
                                tint = iconTint
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        BigCircleSendPhotoAction(
                            onClick = {
                                mainState.audioFile?.let { file ->
                                    saveToMediaStore(context, file)?.let { uri ->
                                        Toast.makeText(
                                            context,
                                            "Аудио сохранено: $uri",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                resetToInitialState()
                            }
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(
                            onClick = { showKeyboardTrigger = true },
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { showKeyboardTrigger = true }
                        ) {
                            Icon(
                                Icons.Outlined.TextFields,
                                modifier = Modifier.size(40.dp),
                                contentDescription = stringResource(R.string.recorder_show_keyboard_content_description),
                                tint = iconTint
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = 35.dp)
                        .padding(bottom = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.recorder_more_content_description),
                        tint = Color(0xFFEDEEF2).copy(alpha = 0.65f),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                //if (mainState.currentState == "STATE_1") Spacer(Modifier.height(63.dp))

                val isRecording = mainState.currentState == "STATE_1" && mainState.isRecording

                BigCircleMicroButton(
                    onClick = {
                        // on_long_press
                    },
                    onLongPress = {
                        longPressStart()
                    },
                    onLongPressEnd = {
                        longPressEnd()
                    },
                    modifier = Modifier,
                    isRecording = isRecording
                )
            }

            if (isImage2Visible && mainState.currentState != "STATE_2") {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = 35.dp)
                        .padding(bottom = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.recorder_more_content_description),
                        tint = Color(0xFFEDEEF2).copy(alpha = 0.65f),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
    }
}

private fun formatElapsedTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val millis = (milliseconds % 1000) / 10

    return if (minutes > 0) {
        String.format("%d:%02d:%02d", minutes, seconds, millis)
    } else {
        String.format("%02d:%02d", seconds, millis)
    }
}

@Preview()
@Composable
fun preview(){
    RecorderScreen({}, {}, {}, {}, )
}
