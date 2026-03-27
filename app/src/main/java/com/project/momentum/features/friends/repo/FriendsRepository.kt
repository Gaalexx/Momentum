package com.project.momentum.features.friends.repo

import com.example.Models.FriendRequestActionDTO
import com.project.momentum.features.friends.api.FriendsInfoAPI
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.ui.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val friendsInfoAPI: FriendsInfoAPI
) {

    sealed interface RequestBy {
        val identifier: String

        data class ByEmail(override val identifier: String) : RequestBy
        data class ByNumber(override val identifier: String) : RequestBy
        data class ByLogin(override val identifier: String) : RequestBy
    }

    suspend fun getAllFriends(): List<User> {
        val friendsDTO = friendsInfoAPI.getFriends()

        return friendsDTO.friends.map { friend ->
            User(
                id = friend.userId,
                name = friend.username,
                avatarUrl = friend.userAvatarUrl
            )
        }
    }

    suspend fun getAllRequests(): List<FriendRequest> {
        val requestsDTO = friendsInfoAPI.getIncomingRequests()

        return requestsDTO.map { request ->
            FriendRequest(
                request.id,
                request.fromUserId,
                request.fromUserUsername,
                request.fromUserAvatarUrl
            )
        }
    }

    suspend fun acceptFriendRequest(
        requestId: String
    ): Boolean {
        val response: FriendRequestActionDTO
        try {
            response = friendsInfoAPI.acceptIncomingRequest(requestId)
            return response.success
        } catch (ex: Exception) {
            println("Error while accepting friend request")
        }
        return false
    }

    suspend fun rejectFriendRequest(
        requestId: String
    ): Boolean {
        val response: FriendRequestActionDTO

        try {
            response = friendsInfoAPI.rejectIncomingRequest(requestId)
            friendsInfoAPI.deleteRequest(requestId)
            return response.success
        } catch (ex: Exception) {
            println("Error while deleting friend request")
        }
        return false
    }

    suspend fun createFriendRequest(    // TODO допилить обратную связь, если пользователя не существует или другие ошибки
        requestBy: RequestBy
    ) {
        when (requestBy) {
            is RequestBy.ByEmail -> {
                // TODO сделать проверку что такой пользователь вообще есть
                friendsInfoAPI.createRequestWithEmail(requestBy.identifier)
            }

            is RequestBy.ByLogin -> println("Not implemented")
            is RequestBy.ByNumber -> println("Not implemented")
        }
    }

}
