package com.project.momentum.features.contentcreation.repos

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import com.project.momentum.di.IoDispatcher
import com.project.momentum.features.contentcreation.mediaconfig.AudioRecordingFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File
import javax.inject.Inject

class AudioRecorderRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    private companion object {
        const val TAG = "RECORDER REPOSITORY"
    }

    private var _audioRecording: MediaRecorder? = null

    fun onRecorderSwitch() {
        if (_audioRecording != null) {
            _audioRecording!!.stop()
            _audioRecording!!.release()
            _audioRecording = null
            return
        }

        val outputFile = File(context.filesDir, AudioRecordingFormat.FILE_NAME)
        _audioRecording = MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.OGG)
            setOutputFile(outputFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
            setAudioChannels(AudioRecordingFormat.CHANNELS)
            setAudioEncodingBitRate(AudioRecordingFormat.BIT_RATE)
            setAudioSamplingRate(AudioRecordingFormat.SAMPLE_RATE)
            try {
                prepare()
            } catch (e: Exception) {
                Log.e(TAG, "prepare() failed")
                Toast.makeText(context, "Audio recorder failed", Toast.LENGTH_LONG).show()
            }
        }
        _audioRecording!!.start()
    }

    fun recordingIsActive(): Boolean {
        return _audioRecording != null
    }


}