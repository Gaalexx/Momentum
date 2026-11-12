package com.project.momentum.data.posts.remote

import com.project.momentum.data.media.remote.MediaTypeDto
import kotlinx.serialization.Serializable

@Serializable
data class ReactionsDTO(val emoji: String, val users: List<String>)

@Serializable
data class PostDTO(
    val id: String,
    val userId: String,
    val userName: String,
    val title: String,
    val inUse: Boolean,
    val presignedURL: String,
    val mediaType: MediaTypeDto,
    val reactions: List<ReactionsDTO>,
    val avatarPresignedURL: String? = null,
    val createdAt: String? = null
)
