package com.project.momentum.domain.friends.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val description: String? = null,
    val hasPremium: Boolean = false
)

data class FriendRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val avatarUrl: String? = null
)
