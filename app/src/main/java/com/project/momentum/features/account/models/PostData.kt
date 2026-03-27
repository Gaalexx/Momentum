package com.project.momentum.features.account.models

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class PostData(
    val id: String,
    val userId: String,
    val userName: String,
    val title: String,
    val presignedURL: String,
    val avatarPresignedURL: String? = null,
    val createdAt: String? = null
) {
    fun getDate(): String {
        val instant = Instant.parse(createdAt)

        return instant
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }
}