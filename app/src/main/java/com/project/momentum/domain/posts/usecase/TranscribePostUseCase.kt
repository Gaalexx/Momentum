package com.project.momentum.domain.posts.usecase

import com.project.momentum.domain.posts.model.TranscriptionStatus
import com.project.momentum.domain.posts.repository.PostsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/** Starts transcription and polls until completion, failure, cancellation or timeout. */
class TranscribePostUseCase(
    private val repository: PostsRepository,
    private val pollInterval: Duration = 8.seconds,
    private val timeout: Duration = 5.minutes
) {
    suspend operator fun invoke(postId: String): String? {
        if (!repository.sendPostForTranscription(postId)) return null
        return withTimeoutOrNull(timeout) {
            var answer = repository.checkPostTranscriptionState(postId)
            while (answer.status == TranscriptionStatus.TRANSCRIPTING) {
                delay(pollInterval)
                answer = repository.checkPostTranscriptionState(postId)
            }
            answer.transcription.takeIf { answer.status == TranscriptionStatus.DONE }
        }
    }
}
