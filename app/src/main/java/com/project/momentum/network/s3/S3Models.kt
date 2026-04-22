package com.project.momentum.network.s3

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
data class UploadAvatarInfoDTO(
    val mimeType: String,
    val size: Long,
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

@Serializable
data class ReactionsDTO(
    val emoji: String,
    val users: List<String>
)

@Serializable
data class PostDTO(
    val id: String,
    val userId: String,
    val userName: String,
    val title: String,
    val inUse: Boolean,
    val presignedURL: String,
    val mediaType: MediaType,
    val reactions: List<ReactionsDTO>,
    val avatarPresignedURL: String? = null,
    val createdAt: String? = null
)

