package com.project.momentum.data.s3

import kotlinx.serialization.Serializable

@Serializable
enum class UploadingStatus(val value: String) {
    UPLOADING("UPLOADING"),
    READY("READY"),
    FAILED("FAILED")
}

@Serializable
enum class MediaType(val value: String) {
    IMAGE("IMAGE"),
    AUDIO("AUDIO"),
    VIDEO("VIDEO")
}

@Serializable
data class UploadInfoDTO(
    val mimeType: String,
    val mediaType: MediaType,
    val size: Long,
    val durationMs: Long? = null,
)

@Serializable
data class S3UpdateStatusDTO(
    val status: UploadingStatus,
    val mediaId: String,
    val title: String? = null,
)

@Serializable
data class PresignedURLDTO(
    val urlToLoad: String,
    val mediaId: String
)
