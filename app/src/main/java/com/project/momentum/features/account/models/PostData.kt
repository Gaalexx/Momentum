package com.project.momentum.features.account.models

import com.project.momentum.features.posts.features.reactions.models.ReactionData
import com.project.momentum.network.s3.MediaType
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class PostData(
    val id: String,
    val userId: String,
    val userName: String,
    val title: String,
    val presignedURL: String,
    val mediaType: MediaType,
    val avatarPresignedURL: String? = null,
    val reactions: List<ReactionData>? = null,
    val createdAt: String? = null
) {
    fun getDate(): String {
        val instant = createdAtInstantOrNull() ?: return ""

        return instant
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }

    fun createdAtInstantOrNull(): Instant? =
        createdAt?.let { value ->
            runCatching { Instant.parse(value) }.getOrNull()
        }
}
