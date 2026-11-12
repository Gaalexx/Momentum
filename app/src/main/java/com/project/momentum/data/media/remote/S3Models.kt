package com.project.momentum.data.media.remote

import kotlinx.serialization.Serializable

@Serializable
enum class UploadingStatus(val value: String) {
    UPLOADING("UPLOADING"),
    READY("READY"),
    FAILED("FAILED")
}

@Serializable
enum class MediaTypeDto(val value: String) {
    IMAGE("IMAGE"),
    AUDIO("AUDIO"),
    VIDEO("VIDEO")
}

@Serializable
data class UploadInfoDTO(
    val mimeType: String,
    val mediaType: MediaTypeDto,
    val size: Long,
    val durationMs: Long? = null,
)

@Serializable
data class UploadAvatarInfoDTO(
    val mimeType: String,
    val size: Long,
)

@Serializable
data class S3UpdateStatusDTO(
    val status: UploadingStatus,
    val mediaId: String,
    val title: String? = null,
    val receiverIds: List<String> = emptyList()
)

@Serializable
data class PresignedURLDTO(
    val urlToLoad: String,
    val mediaId: String
)
