package com.project.momentum.features.contentcreation.media

internal object AudioRecordingFormat {
    const val FILE_EXTENSION = "ogg"
    const val STORAGE_MIME_TYPE = "audio/ogg"
    const val UPLOAD_MIME_TYPE = "audio/ogg;codecs=opus"
    const val BIT_RATE = 32_000
    const val SAMPLE_RATE = 48_000
    const val CHANNELS = 1
}
