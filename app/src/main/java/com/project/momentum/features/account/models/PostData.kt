package com.project.momentum.features.account.models

import kotlinx.serialization.Serializable

@Serializable
data class PostData(
    val id: String,
    val userId: String,
    val title: String,
    val presignedURL: String,
    val createdAt: String? = null
)