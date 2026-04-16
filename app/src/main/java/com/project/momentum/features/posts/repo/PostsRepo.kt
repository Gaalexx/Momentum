package com.project.momentum.features.posts.repo

import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.api.PostsAPI
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepo @Inject constructor(
    private val postsAPI: PostsAPI
) {

    suspend fun getAllPosts(): List<PostData> {
        val myPostsDTO = postsAPI.getMyPosts()
        val myFriendsPostsDTO = postsAPI.getMyFriendsPosts()

        val result: MutableList<PostData> = mutableListOf()
        myPostsDTO.forEach { it ->
            result.add(
                PostData(
                    id = it.id,
                    userId = it.userId,
                    userName = it.userName,
                    title = it.title,
                    presignedURL = it.presignedURL,
                    avatarPresignedURL = it.avatarPresignedURL,
                    mediaType = it.mediaType,
                    createdAt = it.createdAt
                )
            )
        }
        myFriendsPostsDTO.forEach { it ->
            result.add(
                PostData(
                    id = it.id,
                    userId = it.userId,
                    userName = it.userName,
                    title = it.title,
                    presignedURL = it.presignedURL,
                    avatarPresignedURL = it.avatarPresignedURL,
                    mediaType = it.mediaType,
                    createdAt = it.createdAt
                )
            )
        }

        result.sortByDescending { it.createdAtInstantOrNull() ?: Instant.MIN }

        return result
    }

}
