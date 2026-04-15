package com.project.momentum.features.contentcreation.ui

import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.project.momentum.features.contentcreation.data.MediaTypeToSend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.sqrt

@Stable
class AudioRecordingController(
    context: Context,
    private val scope: CoroutineScope,
    private val maxRecordMs: Int,
) {
    private val context = context.applicationContext

    var isRecording by mutableStateOf(false)
        private set

    var progress by mutableFloatStateOf(0f)
        private set

    var amplitudeLevel by mutableFloatStateOf(0f)
        private set

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var progressJob: Job? = null
    private var amplitudeJob: Job? = null
    private var stopJob: Job? = null

    fun start(
        onSaved: (Uri, MediaTypeToSend) -> Unit,
    ) {
        if (isRecording || stopJob?.isActive == true) return

        val file = createTempAudioFile(context)
        val newRecorder = createRecorder(file)

        try {
            newRecorder.prepare()
            newRecorder.start()
        } catch (e: Exception) {
            Log.e(TAG, "Unable to start audio recording", e)
            newRecorder.releaseSafely()
            file.delete()
            return
        }

        recorder = newRecorder
        outputFile = file
        isRecording = true
        progress = 0f
        launchProgress(onSaved)
        launchAmplitudeSampling(newRecorder)
    }

    fun stop(
        onSaved: (Uri, MediaTypeToSend) -> Unit,
    ) {
        val activeRecorder = recorder ?: return
        val file = outputFile ?: return

        recorder = null
        outputFile = null
        isRecording = false
        amplitudeLevel = 0f
        progressJob?.cancel()
        progressJob = null
        amplitudeJob?.cancel()
        amplitudeJob = null

        val hasValidRecording = stopRecorder(activeRecorder)
        if (!hasValidRecording) {
            file.delete()
            progress = 0f
            return
        }

        stopJob = scope.launch {
            val savedUri = withContext(Dispatchers.IO) {
                saveAudioToMediaStore(context, file)
            }
            file.delete()
            progress = 0f

            if (savedUri != null) {
                onSaved(savedUri, MediaTypeToSend.AUDIO)
            }
        }
    }

    fun reset() {
        recorder?.let(::stopRecorder)
        recorder = null
        outputFile?.delete()
        outputFile = null
        isRecording = false
        progress = 0f
        amplitudeLevel = 0f
        progressJob?.cancel()
        progressJob = null
        amplitudeJob?.cancel()
        amplitudeJob = null
    }

    fun dispose() {
        reset()
        stopJob?.cancel()
        stopJob = null
    }

    private fun launchProgress(
        onSaved: (Uri, MediaTypeToSend) -> Unit,
    ) {
        progressJob?.cancel()
        progressJob = scope.launch {
            val startedAt = System.currentTimeMillis()

            while (isRecording) {
                val elapsed = System.currentTimeMillis() - startedAt
                progress = (elapsed.toFloat() / maxRecordMs).coerceIn(0f, 1f)

                if (elapsed >= maxRecordMs) {
                    stop(onSaved)
                    break
                }

                delay(PROGRESS_TICK_MS)
            }
        }
    }

    private fun launchAmplitudeSampling(activeRecorder: MediaRecorder) {
        amplitudeJob?.cancel()
        amplitudeJob = scope.launch {
            while (isRecording) {
                amplitudeLevel = activeRecorder.currentAmplitudeLevel()
                delay(AMPLITUDE_TICK_MS)
            }
        }
    }

    private fun stopRecorder(activeRecorder: MediaRecorder): Boolean {
        val stopped = try {
            activeRecorder.stop()
            true
        } catch (e: RuntimeException) {
            Log.e(TAG, "Unable to stop audio recording", e)
            false
        } finally {
            activeRecorder.releaseSafely()
        }

        return stopped
    }

    private fun createRecorder(file: File): MediaRecorder {
        return MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(file.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(AUDIO_BIT_RATE)
            setAudioSamplingRate(AUDIO_SAMPLE_RATE)
        }
    }

    private fun createTempAudioFile(context: Context): File {
        val fileName = "MOMENTUM_AUD_" + SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.US,
        ).format(System.currentTimeMillis()) + ".m4a"

        return File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            fileName,
        )
    }

    private fun saveAudioToMediaStore(context: Context, audioFile: File): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, audioFile.name)
            put(MediaStore.MediaColumns.MIME_TYPE, AUDIO_MIME_TYPE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Audio.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_MUSIC + "/Momentum",
                )
                put(MediaStore.Audio.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            ?: return null

        return try {
            resolver.openOutputStream(uri)?.use { outputStream ->
                audioFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: error("ContentResolver returned null output stream")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val publishedValues = ContentValues().apply {
                    put(MediaStore.Audio.Media.IS_PENDING, 0)
                }
                resolver.update(uri, publishedValues, null, null)
            }

            uri
        } catch (e: Exception) {
            Log.e(TAG, "Unable to save audio to MediaStore", e)
            resolver.delete(uri, null, null)
            null
        }
    }

    private fun MediaRecorder.currentAmplitudeLevel(): Float {
        val rawAmplitude = try {
            maxAmplitude
        } catch (e: RuntimeException) {
            0
        }

        val normalized = (rawAmplitude / MAX_RECORDER_AMPLITUDE).coerceIn(0f, 1f)
        return sqrt(normalized)
    }

    private fun MediaRecorder.releaseSafely() {
        try {
            release()
        } catch (e: RuntimeException) {
            Log.e(TAG, "Unable to release MediaRecorder", e)
        }
    }

    private companion object {
        const val TAG = "AudioRecording"
        const val AUDIO_MIME_TYPE = "audio/mp4"
        const val AUDIO_BIT_RATE = 128_000
        const val AUDIO_SAMPLE_RATE = 44_100
        const val MAX_RECORDER_AMPLITUDE = 32767f
        const val PROGRESS_TICK_MS = 16L
        const val AMPLITUDE_TICK_MS = 45L
    }
}

@Composable
fun rememberAudioRecordingController(
    maxRecordMs: Int,
): AudioRecordingController {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    return remember(context, scope, maxRecordMs) {
        AudioRecordingController(
            context = context,
            scope = scope,
            maxRecordMs = maxRecordMs,
        )
    }
}
