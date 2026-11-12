package com.project.momentum.domain.friends.repository

import com.project.momentum.domain.friends.model.FriendRequest
import com.project.momentum.domain.friends.model.User

interface FriendsRepository {
    sealed interface RequestBy {
        val identifier: String
        data class ByEmail(override val identifier: String) : RequestBy
        data class ByNumber(override val identifier: String) : RequestBy
        data class ByLogin(override val identifier: String) : RequestBy
    }

    suspend fun deleteFriend(friend: User): Boolean
    suspend fun getAllFriends(): List<User>
    suspend fun getAllRequests(): List<FriendRequest>
    suspend fun acceptFriendRequest(requestId: String): Boolean
    suspend fun rejectFriendRequest(requestId: String): Boolean
    suspend fun createFriendRequest(requestBy: RequestBy): Boolean
    suspend fun getUserById(id: String): User
}
