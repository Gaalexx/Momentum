package com.project.momentum.features.account.models

data class PostData(
    val id: String,
    val userId: String,
    val title: String,
    val presignedURL: String,
    val createdAt: String? = null
)