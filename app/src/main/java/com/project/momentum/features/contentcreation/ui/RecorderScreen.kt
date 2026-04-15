package com.project.momentum.features.contentcreation.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.audiofx.Visualizer
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.features.contentcreation.data.MediaTypeToSend
import com.project.momentum.ui.assets.*
import com.project.momentum.ui.theme.ConstColours
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.sqrt
import android.Manifest
import androidx.core.content.ContextCompat
import android.util.Log
import kotlinx.coroutines.CoroutineScope

@Composable
fun AudioRadialVisualizer(amplitude: Float) {
    val normalized = (amplitude / 2000f)
        .coerceIn(0f, 1f)

    val animated by animateFloatAsState(
        targetValue = normalized,
        animationSpec = tween(80)
    )

    val infinite = rememberInfiniteTransition()
    val wave by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val normalized = (amplitude / 1200f)
            .coerceIn(0f, 1f)

        val boosted = sqrt(normalized)
        val reactiveWave = wave * 0.4f * (0.3f + animated)
        val motion = boosted * 1.3f + reactiveWave

        val maxRadius = size.maxDimension
        val shift = size.minDimension * 0.5f * motion

        val dynamicCenter = Offset(
            x = center.x + shift,
            y = center.y - shift * 0.6f
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF3A86FF).copy(alpha = 0.5f + animated * 0.6f),
                    Color(0xFFFF006E).copy(alpha = 0.3f + animated * 0.6f),
                    Color.Transparent
                ),
                center = dynamicCenter,
                radius = maxRadius
            )
        )

        val radius = maxRadius * (0.25f + motion * 0.6f)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFF006E),
                    Color(0xFF3A86FF),
                    Color.Transparent
                ),
                radius = radius
            ),
            radius = radius,
            center = center
        )
    }
}

@SuppressLint("MissingPermission")
fun startAudioPreview(context: Context, uri: Uri, mainState: MainState) {
    try {
        val player = MediaPlayer().apply {
            setDataSource(context, uri)
            prepare()
            start()
            isLooping = true
        }

        val visualizer = Visualizer(player.audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]

            setDataCaptureListener(
                object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(
                        visualizer: Visualizer?,
                        waveform: ByteArray?,
                        samplingRate: Int
                    ) {
                        waveform?.let {
                            var sum = 0.0
                            for (b in it) {
                                sum += b * b
                            }
                            val rms = sqrt(sum / it.size).toFloat()
                            mainState.amplitude = rms
                        }
                    }

                    override fun onFftDataCapture(
                        visualizer: Visualizer?,
                        fft: ByteArray?,
                        samplingRate: Int
                    ) {}
                },
                Visualizer.getMaxCaptureRate() / 2,
                true,
                false
            )

            enabled = true
        }

        mainState.mediaPlayer = player
        mainState.visualizer = visualizer

        mainState.isPlaying = true
        mainState.startProgressUpdate()

    } catch (e: Exception) {
        Log.e("AudioPreview", "Error starting preview: ${e.message}", e)
    }
}

fun stopAudioPreview(mainState: MainState) {
    mainState.visualizer?.release()
    mainState.visualizer = null
    mainState.mediaPlayer?.stop()
    mainState.mediaPlayer?.release()
    mainState.mediaPlayer = null
    mainState.amplitude = 0f
}

@SuppressLint("MissingPermission")
fun startAudioCapture(mainState: MainState) {
    val sampleRate = 44100
    val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    val audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    mainState.audioRecord = audioRecord
    mainState.isFftRunning = true

    val buffer = ShortArray(bufferSize)
    val scope = CoroutineScope(Dispatchers.Main)

    Thread {
        audioRecord.startRecording()

        while (mainState.isFftRunning) {
            val read = audioRecord.read(buffer, 0, buffer.size)

            if (read > 0) {
                var sum = 0.0
                for (i in 0 until read) {
                    sum += buffer[i] * buffer[i]
                }
                val rms = sqrt(sum / read).toFloat()
                scope.launch {
                    mainState.amplitude = rms
                }
            }
        }

        audioRecord.stop()
        audioRecord.release()
    }.start()
}

fun stopAudioCapture(mainState: MainState) {
    mainState.isFftRunning = false
}

@Composable
fun rememberMicrophonePermissionState(): State<Boolean> {
    val context = LocalContext.current
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
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

    val audioFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
        fileName
    )

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
    onCameraClick: () -> Unit,
    onGoToFriends: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onGoToGallery: () -> Unit,
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
            .pointerInput(Unit) {},
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileCircleButton(
                onClick = onProfileClick,
                backgroundColor = chrome2
            )

            Spacer(Modifier.weight(1f))

            FriendsPillButton(onClick = onGoToFriends)

            Spacer(Modifier.weight(1f))

            SettingsCircleButton(
                onClick = onGoToSettings,
                backgroundColor = chrome2
            )
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(60.dp))
                .background(ConstColours.BLACK)
        ) {
            if ((mainState.currentState == "STATE_1" && mainState.isRecording) ||
                (mainState.currentState == "STATE_2" && mainState.isPlaying)) {
                RecordingProgressRing(
                    progress = if (mainState.currentState == "STATE_2")
                        mainState.playbackProgress
                    else
                        mainState.recordingProgress,
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
                if (mainState.currentState == "STATE_1" || mainState.currentState == "STATE_2") {
                    AudioRadialVisualizer(mainState.amplitude)
                } else {
                    AsyncImage(
                        model = stringResource(R.string.rec_img_model_),
                        contentDescription = stringResource(R.string.recorder_main_image_content_description),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (mainState.currentState == "STATE_2") {
                    val context = LocalContext.current
                    val previewUri = mainState.audioFile?.let { Uri.fromFile(it) }

                    LaunchedEffect(Unit) {
                        previewUri?.let {
                            Log.d("STATE_2", "Starting audio preview")
                            startAudioPreview(context, it, mainState)
                        }
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            Log.d("STATE_2", "Stopping audio preview")
                            mainState.stopProgressUpdate()
                            stopAudioPreview(mainState)
                        }
                    }

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
                    onClick = onCameraClick,
                    icon = Icons.Outlined.PhotoCamera,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )

                CircleButton(
                    size = 60.dp,
                    onClick = {},
                    icon = Icons.Outlined.Mic,
                    backgroundColor = ConstColours.BLACK,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        SecondaryImagesSection(
            mainState = mainState,
            onGoToPreview = onGoToPreview,
            onGoToGallery = onGoToGallery
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
    var audioRecord: AudioRecord? by mutableStateOf(null)
    var fftData by mutableStateOf<FloatArray?>(null)
    var amplitude by mutableStateOf(0f)
    var isFftRunning by mutableStateOf(false)
    var mediaPlayer: MediaPlayer? by mutableStateOf(null)
    var visualizer: Visualizer? by mutableStateOf(null)
    var previewRestartKey by mutableStateOf(0)
    var playbackProgress by mutableStateOf(0f)

    var isPlaying by mutableStateOf(false)
    var progressUpdateJob: Job? = null

    fun startProgressUpdate() {
        progressUpdateJob?.cancel()
        progressUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isPlaying && mediaPlayer != null) {
                delay(50)
                val current = mediaPlayer?.currentPosition ?: 0
                val duration = mediaPlayer?.duration ?: 1
                playbackProgress = current.toFloat() / duration.toFloat()
            }
        }
    }

    fun stopProgressUpdate() {
        isPlaying = false
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }
}

@Composable
fun SecondaryImagesSection(
    mainState: MainState,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onGoToGallery: () -> Unit
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
        val newState = when {
            !isImage1Tinted && !isLongPressActive -> "INITIAL"
            isImage1Tinted && isLongPressActive -> "STATE_1"
            isImage1Tinted && !isLongPressActive -> "STATE_2"
            else -> "INITIAL"
        }

        when (newState) {
            "STATE_2" -> {
                stopAudioCapture(mainState)
            }
            "STATE_1" -> {
                if (mainState.currentState == "STATE_2") {
                    startAudioCapture(mainState)
                }
            }
            "INITIAL" -> {
                stopAudioCapture(mainState)
                stopRecording(mainState)
            }
        }

        mainState.currentState = newState

        if (newState == "STATE_2") {
            delay(100)
            captionFocusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopRecording(mainState)
            stopAudioCapture(mainState)
            mainState.audioFile?.delete()
            progressJob?.cancel()
        }
    }

    fun resetToInitialState() {
        mainState.stopProgressUpdate()
        stopAudioPreview(mainState)
        stopRecording(mainState)
        stopAudioCapture(mainState)

        mainState.audioFile?.delete()
        mainState.audioFile = null

        isImage1Tinted = false
        isImage2Visible = true
        isLongPressActive = false
        recordingStarted = false
        stopRequested = false
        progressJob?.cancel()
        progressJob = null
        mainState.recordingProgress = 0f
        mainState.playbackProgress = 0f
        mainState.previewRestartKey = 0
        keyboardController?.hide()
    }

    fun longPressEnd() {
        if (!isImage1Tinted) return

        if (!mainState.isRecording) {
            stopRequested = true
            return
        }

        stopRecording(mainState)
        stopAudioCapture(mainState)
        progressJob?.cancel()
        mainState.recordingProgress = 0f

        isLongPressActive = false
    }


    fun startProgressAnimation() {
        progressJob?.cancel()
        progressJob = scope.launch {
            val startTime = System.currentTimeMillis()

            while (isActive && mainState.isRecording) {
                val elapsed = System.currentTimeMillis() - startTime
                mainState.recordingProgress = (elapsed.toFloat() / mainState.maxRecordMs)
                    .coerceIn(0f, 1f)

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
            startAudioCapture(mainState)
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
                                    mainState.stopProgressUpdate()
                                    stopAudioPreview(mainState)

                                    val savedUri = saveToMediaStore(context, file)

                                    file.delete()
                                    mainState.audioFile = null
                                    resetToInitialState()

                                    // TODO: Здесь можно добавить сохранение URI в базу данных или уведомление
                                    // НЕ использовать onGoToPreview, чтобы не открывать другой экран!
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
                val isRecording = mainState.currentState == "STATE_1" && mainState.isRecording

                BigCircleMicroButton(
                    onClick = { },
                    onLongPress = { longPressStart() },
                    onLongPressEnd = { longPressEnd() },
                    modifier = Modifier,
                    isRecording = isRecording
                )
            }

            if (isImage2Visible && mainState.currentState != "STATE_2") {
                IconButton(
                    onClick = onGoToGallery,
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = 35.dp)
                        .padding(bottom = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.recorder_more_content_description),
                        tint = Color(0xFFEDEEF2).copy(alpha = 0.6f),
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
fun preview() {
    RecorderScreen(
        {},
        {},
        {},
        {},
        { _, _ -> },
        {}
    )
}