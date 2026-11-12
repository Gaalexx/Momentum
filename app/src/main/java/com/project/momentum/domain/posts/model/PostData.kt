package com.project.momentum.domain.posts.model

import com.project.momentum.domain.media.model.MediaType
import java.time.Instant

data class PostData(
    val id: String,
    val userId: String,
    val userName: String,
    val title: String,
    val presignedURL: String,
    val mediaType: MediaType,
    val isOwner: Boolean,
    val avatarPresignedURL: String? = null,
    val reactions: List<ReactionData>? = null,
    val createdAt: String? = null,
    val transcription: String? = ""
) {

    constructor(post: PostData) : this(
        post.id,
        post.userId,
        post.userName,
        post.title,
        post.presignedURL,
        post.mediaType,
        post.isOwner,
        post.avatarPresignedURL,
        post.reactions,
        post.createdAt,
        post.transcription
    )

    constructor(post: PostData, newTranscription: String?) : this(
        post.id,
        post.userId,
        post.userName,
        post.title,
        post.presignedURL,
        post.mediaType,
        post.isOwner,
        post.avatarPresignedURL,
        post.reactions,
        post.createdAt,
        newTranscription
    )

    fun createdAtInstantOrNull(): Instant? =
        createdAt?.let { value ->
            runCatching { Instant.parse(value) }.getOrNull()
        }
}
