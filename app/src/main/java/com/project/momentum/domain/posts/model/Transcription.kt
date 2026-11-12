package com.project.momentum.domain.posts.model

enum class TranscriptionStatus { DONE, TRANSCRIPTING, ERROR }

data class Transcription(val status: TranscriptionStatus, val transcription: String)
