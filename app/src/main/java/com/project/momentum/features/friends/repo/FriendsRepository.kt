package com.project.momentum.features.friends.repo

import com.example.Models.FriendRequestActionDTO
import com.project.momentum.data.usersinfo.UsersInfoAPI
import com.project.momentum.features.friends.api.FriendsInfoAPI
import com.project.momentum.features.friends.api.VkCommandAPI
import com.project.momentum.features.friends.api.dtos.VkFriend
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.ui.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val friendsInfoAPI: FriendsInfoAPI,
    private val usersInfoAPI: UsersInfoAPI,
    private val vkAPI: VkCommandAPI
) {

    sealed interface RequestBy {
        val identifier: String

        data class ByEmail(override val identifier: String) : RequestBy
        data class ByNumber(override val identifier: String) : RequestBy
        data class ByLogin(override val identifier: String) : RequestBy
    }

    suspend fun deleteFriend(friend: User): Boolean {
        return friendsInfoAPI.deleteFriendshipWith(friend.id)
    }

    suspend fun getAllFriends(): List<User> {
        val friendsDTO = friendsInfoAPI.getFriends()

        return friendsDTO.friends.map { friend ->
            User(
                id = friend.userId,
                name = friend.username,
                email = friend.email,
                phoneNumber = friend.phoneNumber,
                isOnline = false,
                description = friend.description,
                avatarUrl = friend.userAvatarUrl,
                hasPremium = friend.hasPremium
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

    suspend fun getUserById(id: String): User {
        val response = usersInfoAPI.getUserById(id)
        return User(
            id = response.userId,
            name = response.name,
            email = response.email,
            phoneNumber = response.phone,
            avatarUrl = response.profilePhotoURL,
            hasPremium = response.hasPremium
        )
    }

    suspend fun getVkFriends(): List<VkFriend> = vkAPI.getFriendsFromVK()

}
