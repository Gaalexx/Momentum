package com.project.momentum.features.posts.repo

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.api.PostsAPI
import com.project.momentum.features.posts.features.reactions.models.ReactionData
import com.project.momentum.features.posts.features.reactions.models.ReactionType
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepo @Inject constructor(
    private val postsAPI: PostsAPI,
    private val sessionManager: SessionManager
) {

    suspend fun getAllPosts(): List<PostData> {
        val myPostsDTO = postsAPI.getMyPosts()
        val myFriendsPostsDTO = postsAPI.getMyFriendsPosts()
        val currentUser = sessionManager.getUserId() ?: throw Exception("Unauthenticated")

        val result: MutableList<PostData> = mutableListOf()

        listOf(myPostsDTO, myFriendsPostsDTO).forEach { posts ->
            posts.forEach { it ->
                result.add(
                    PostData(
                        id = it.id,
                        userId = it.userId,
                        userName = it.userName,
                        title = it.title,
                        presignedURL = it.presignedURL,
                        avatarPresignedURL = it.avatarPresignedURL,
                        mediaType = it.mediaType,
                        isOwner = it.userId == currentUser,
                        reactions = it.reactions.map {
                            ReactionData(
                                emoji = ReactionType.valueOf(it.emoji),
                                users = it.users
                            )
                        },
                        createdAt = it.createdAt
                    )
                )
            }
        }

        result.sortByDescending { it.createdAtInstantOrNull() ?: Instant.MIN }

        return result
    }

    suspend fun deletePost(postId: String) : Boolean =
        postsAPI.deletePost(postId)

    suspend fun sendReaction(postId: String, reaction: ReactionType) : Boolean =
        postsAPI.sendReaction(postId, reaction)

    suspend fun deleteReaction(postId: String, reaction: ReactionType) : Boolean =
        postsAPI.deleteReaction(postId, reaction)

    suspend fun getHiddenPosts() : List<String> =
        postsAPI.getHiddenPosts()

    suspend fun hidePost(postId: String) : Boolean =
        postsAPI.hidePost(postId)

    suspend fun showPost(postId: String) : Boolean =
        postsAPI.showPost(postId)
}
