package com.project.momentum.features.posts.repo

import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.friends.api.FriendsInfoAPI
import com.project.momentum.features.posts.api.PostsAPI
import java.time.Instant
import java.time.ZoneId
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
                    it.id,
                    it.userId,
                    it.userName,
                    it.title,
                    it.presignedURL,
                    it.createdAt
                )
            )
        }
        myFriendsPostsDTO.forEach { it ->
            result.add(
                PostData(
                    it.id,
                    it.userId,
                    it.userName,
                    it.title,
                    it.presignedURL,
                    it.createdAt
                )
            )
        }

        result.sortWith(Comparator { data, data1 ->
            val time = Instant.parse(data.createdAt).atZone(ZoneId.systemDefault())
            val time1 = Instant.parse(data1.createdAt).atZone(ZoneId.systemDefault())
            return@Comparator time1.compareTo(time)
        })

        return result
    }

}