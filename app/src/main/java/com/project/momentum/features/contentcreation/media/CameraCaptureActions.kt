package com.project.momentum.features.contentcreation.media

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.project.momentum.R
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import java.text.SimpleDateFormat
import java.util.Locale

class CameraCaptureActions(context: Context) {
    private val context = context.applicationContext

    fun takePhoto(
        imageCapture: ImageCapture,
        isFrontCamera: Boolean,
        onSaved: (Uri, MediaTypeToSend) -> Unit,
        onError: (Exception) -> Unit = {},
    ) {
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Momentum")
        }

        val metadata = ImageCapture.Metadata().apply {
            isReversedHorizontal = isFrontCamera
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues,
            )
            .setMetadata(metadata)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri
                    if (savedUri != null) {
                        onSaved(savedUri, MediaTypeToSend.PHOTO)
                    } else {
                        onError(IllegalStateException("ImageCapture returned a null Uri"))
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    onError(exc)
                }
            },
        )
    }

    fun startRecording(
        videoCapture: VideoCapture<Recorder>,
        onSaved: (Uri, MediaTypeToSend) -> Unit,
        onEvent: (VideoRecordEvent) -> Unit = {},

        ): Recording {
        val hasAudioPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO,
        ) == PackageManager.PERMISSION_GRANTED

        val name = "MOMENTUM_VID_${System.currentTimeMillis()}.mp4"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Momentum")
            }
        }

        val outputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        ).setContentValues(contentValues)
            .build()

        val pendingRecording = if (hasAudioPermission) {
            videoCapture.output.prepareRecording(context, outputOptions).withAudioEnabled()
        } else {
            videoCapture.output.prepareRecording(context, outputOptions)
        }

        return pendingRecording.start(ContextCompat.getMainExecutor(context)) { event ->
            onEvent(event)
            if (event is VideoRecordEvent.Finalize) {
                if (event.hasError()) {
                    Log.e(
                        TAG,
                        "Video record error=${event.error}",
                        event.cause,
                    )
                    Toast.makeText(
                        context,
                        videoRecordErrorMessage(event.error),
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.recorder_video_saved),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    fun stopRecording(recording: Recording?) {
        recording?.stop()
    }

    private fun videoRecordErrorMessage(error: Int): String {
        return when (error) {
            VideoRecordEvent.Finalize.ERROR_NO_VALID_DATA -> {
                context.getString(
                    R.string.recorder_video_error,
                    context.getString(R.string.recorder_video_error_no_data),
                )
            }

            else -> {
                context.getString(
                    R.string.recorder_video_error,
                    context.getString(R.string.recorder_video_error_unknown),
                )
            }
        }
    }

    private companion object {
        const val TAG = "MediaCreationScreen"
    }
}

@Composable
fun rememberCameraCaptureActions(): CameraCaptureActions {
    val context = LocalContext.current
    return remember(context) { CameraCaptureActions(context) }
}
