package com.project.momentum.features.posts.models.dtos

import kotlinx.serialization.Serializable

@Serializable
enum class TranscriptionStatus(value: String) {
    DONE("done"),
    TRANSCRIPTING("transcripting"),
    ERROR("error")
}

@Serializable
data class GetTranscriptionRequestDTO(
    val postId: String
)


@Serializable
data class GetTranscriptionResponseDTO(
    val status: TranscriptionStatus,
    val transcription: String
)
