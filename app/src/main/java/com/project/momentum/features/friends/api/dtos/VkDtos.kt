package com.project.momentum.features.friends.api.dtos

data class VkFriend(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val photo200: String?,
    val online: Boolean
)