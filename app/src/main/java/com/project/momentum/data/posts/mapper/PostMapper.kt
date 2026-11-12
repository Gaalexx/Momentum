package com.project.momentum.data.posts.mapper

import com.project.momentum.data.posts.remote.GetTranscriptionResponseDTO
import com.project.momentum.data.posts.remote.PostDTO
import com.project.momentum.domain.media.model.MediaType
import com.project.momentum.domain.posts.model.PostData
import com.project.momentum.domain.posts.model.ReactionData
import com.project.momentum.domain.posts.model.ReactionType
import com.project.momentum.domain.posts.model.Transcription
import com.project.momentum.domain.posts.model.TranscriptionStatus

internal fun PostDTO.toDomain(currentUserId: String): PostData = PostData(
    id = id,
    userId = userId,
    userName = userName,
    title = title,
    presignedURL = presignedURL,
    mediaType = MediaType.valueOf(mediaType.name),
    isOwner = userId == currentUserId,
    avatarPresignedURL = avatarPresignedURL,
    reactions = reactions.map { ReactionData(ReactionType.valueOf(it.emoji), it.users) },
    createdAt = createdAt
)

internal fun GetTranscriptionResponseDTO.toDomain(): Transcription =
    Transcription(TranscriptionStatus.valueOf(status.name), transcription)
