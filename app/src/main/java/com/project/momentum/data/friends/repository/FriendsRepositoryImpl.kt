package com.project.momentum.data.friends.repository

import com.project.momentum.data.friends.remote.FriendRequestActionDTO
import com.project.momentum.data.users.remote.UsersInfoAPI
import com.project.momentum.data.friends.remote.FriendsInfoAPI
import com.project.momentum.domain.friends.model.FriendRequest
import com.project.momentum.domain.friends.model.User
import com.project.momentum.domain.friends.repository.FriendsRepository
import com.project.momentum.domain.friends.repository.FriendsRepository.RequestBy
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepositoryImpl @Inject constructor(
    private val friendsInfoAPI: FriendsInfoAPI,
    private val usersInfoAPI: UsersInfoAPI
) : FriendsRepository {

    override suspend fun deleteFriend(friend: User): Boolean {
        return friendsInfoAPI.deleteFriendshipWith(friend.id)
    }

    override suspend fun getAllFriends(): List<User> {
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

    override suspend fun getAllRequests(): List<FriendRequest> {
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

    override suspend fun acceptFriendRequest(
        requestId: String
    ): Boolean {
        val response: FriendRequestActionDTO
        try {
            response = friendsInfoAPI.acceptIncomingRequest(requestId)
            return response.success
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            println("Error while accepting friend request")
        }
        return false
    }

    override suspend fun rejectFriendRequest(
        requestId: String
    ): Boolean {
        val response: FriendRequestActionDTO

        try {
            response = friendsInfoAPI.rejectIncomingRequest(requestId)
            friendsInfoAPI.deleteRequest(requestId)
            return response.success
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            println("Error while deleting friend request")
        }
        return false
    }

    override suspend fun createFriendRequest(    // TODO сделать ошибки конкретнее
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

    override suspend fun getUserById(id: String): User {
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
}
