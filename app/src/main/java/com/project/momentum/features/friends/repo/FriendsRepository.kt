package com.project.momentum.features.friends.repo

import com.example.Models.FriendRequestActionDTO
import com.project.momentum.data.usersinfo.UsersInfoAPI
import com.project.momentum.features.friends.api.FriendsInfoAPI
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.ui.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val friendsInfoAPI: FriendsInfoAPI,
    private val usersInfoAPI: UsersInfoAPI
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

    suspend fun createFriendRequest(    // TODO сделать ошибки конкретнее
        requestBy: RequestBy
    ): Boolean {
        when (requestBy) {
            is RequestBy.ByEmail -> {
                val ifExists = usersInfoAPI.userByEmailExists(requestBy.identifier)
                if (!ifExists) {
                    return false
                }
                friendsInfoAPI.createRequestWithEmail(requestBy.identifier)
                return true
            }

            is RequestBy.ByLogin -> {
                val ifExists = usersInfoAPI.userByLoginExists(requestBy.identifier)
                if (!ifExists) {
                    return false
                }
                friendsInfoAPI.createRequestWithLogin(requestBy.identifier)
                return true
            }
            is RequestBy.ByNumber -> println("Not implemented")
        }
        return false
    }

}
