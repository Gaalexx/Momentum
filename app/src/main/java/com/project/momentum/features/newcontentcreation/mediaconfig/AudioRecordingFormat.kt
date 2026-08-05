package com.project.momentum.features.newcontentcreation.mediaconfig

internal object AudioRecordingFormat {
    const val FILE_EXTENSION = "ogg"
    const val STORAGE_MIME_TYPE = "audio/$FILE_EXTENSION"
    const val UPLOAD_MIME_TYPE = "audio/$FILE_EXTENSION;codecs=opus"
    const val BIT_RATE = 32_000
    const val SAMPLE_RATE = 48_000
    const val CHANNELS = 1
    const val FILE_NAME = "myRec.$FILE_EXTENSION"
}