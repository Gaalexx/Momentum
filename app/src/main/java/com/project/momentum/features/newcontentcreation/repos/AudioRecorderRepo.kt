package com.project.momentum.features.newcontentcreation.repos

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import com.project.momentum.di.IoDispatcher
import com.project.momentum.features.contentcreation.media.AudioRecordingFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

class AudioRecorderRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    private var _audioRecording: MediaRecorder? = null

    fun onRecorderSwitch() {
        if (_audioRecording != null) {
            _audioRecording!!.stop()
            _audioRecording!!.release()
//            _state.update {                   // TODO перенести во вью модель
//                it.copy(isRecording = false)
//            }
            return
        }

        val outputFile = File(context.filesDir, "myRec.${AudioRecordingFormat.FILE_EXTENSION}")
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
                Log.e("AUDIO RECORDER", "prepare() failed")
                Toast.makeText(context, "Audio recorder failed", Toast.LENGTH_LONG).show()
            }
        }
        _audioRecording!!.start()
//        _state.update {                       // TODO перенести во вью модель
//            it.copy(isRecording = true)
//        }
    }

    fun recordingIsActive(): Boolean {
        return _audioRecording != null
    }


}