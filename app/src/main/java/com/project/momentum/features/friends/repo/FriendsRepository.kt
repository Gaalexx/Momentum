package com.project.momentum.features.friends.repo

import com.project.momentum.features.friends.api.FriendsInfoAPI
import com.project.momentum.features.friends.models.FriendRequestActionDTO
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.ui.UserNew
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val friendsInfoAPI: FriendsInfoAPI
) {

    suspend fun getAllFriends(): List<UserNew> {
        val friendsDTO = friendsInfoAPI.getFriends()

        return friendsDTO.friends.map { friend ->
            UserNew(
                id = friend.userId,
                name = friend.username ?: "No name"
            )
        }
    }

    suspend fun getAllRequests(): List<FriendRequest> {
        val requestsDTO = friendsInfoAPI.getIncomingRequests()

        return requestsDTO.map { request ->
            FriendRequest(
                request.id,
                request.fromUserId,
                request.fromUserId, //TODO заменить под имя
                "" // TODO показывать с аватаркой
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

}
