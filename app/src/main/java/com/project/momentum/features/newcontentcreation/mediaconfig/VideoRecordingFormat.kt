package com.project.momentum.features.newcontentcreation.mediaconfig

internal object VideoRecordingFormat {
    const val FILE_EXTENSION = "mp4"
    const val STORAGE_MIME_TYPE = "video/$FILE_EXTENSION"
    const val FILE_NAME = "myRec.${AudioRecordingFormat.FILE_EXTENSION}"
}