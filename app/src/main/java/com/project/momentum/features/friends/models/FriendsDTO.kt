package com.example.Models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID


@Serializable
enum class FriendRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED;

    companion object {
        fun fromString(value: String): FriendRequestStatus? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }

        fun fromStringOrThrow(value: String): FriendRequestStatus {
            return fromString(value)
                ?: throw IllegalArgumentException("Invalid friend request status: $value")
        }
    }
}

@Serializable
enum class FriendRequestUpdateStatus {
    ACCEPTED,
    REJECTED,
    CANCELLED;

    companion object {
        fun fromString(value: String): FriendRequestUpdateStatus? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

@Serializable
data class FriendRequestCreateDTO(
    val toUserId: String
)

@Serializable
data class FriendRequestCreateByEmailDTO(
    val email: String
)

@Serializable
data class FriendRequestResponseDTO(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val status: FriendRequestStatus,
    val createdAt: String,
    val updatedAt: String,
    val userAvatarUrl: String? = null
)

@Serializable
data class FriendRequestWithUserDetailsDTO(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val status: FriendRequestStatus,
    val createdAt: String,
    val updatedAt: String,
    val fromUserUsername: String,
    val toUserUsername: String? = null,
    val fromUserAvatarUrl: String? = null,
    val toUserAvatarUrl: String? = null
)

@Serializable
data class FriendRequestListDTO(
    val incoming: List<FriendRequestWithUserDetailsDTO>,
    val outgoing: List<FriendRequestWithUserDetailsDTO>
)

@Serializable
data class FriendRequestActionDTO(
    val success: Boolean,
    val message: String,
    val requestId: String? = null
)

@Serializable
data class FriendRequestStatusDTO(
    val requestId: String,
    val status: FriendRequestStatus
)

@Serializable
data class FriendRequestUpdateDTO(
    val status: FriendRequestUpdateStatus  // "accepted", "rejected", "cancelled"
)

@Serializable
data class FriendshipResponseDTO(
    val userId: String,
    val username: String,
    val email: String,
    val friendsSince: String,
    val phoneNumber: String? = null,
    val description: String? = null,
    val userAvatarUrl: String? = null,
    val hasPremium: Boolean = false
)

@Serializable
data class FriendshipListDTO(
    val friends: List<FriendshipResponseDTO>
)