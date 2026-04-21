package com.project.momentum.features.posts.api

import android.util.Log
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.posts.features.reactions.models.ReactionType
import com.project.momentum.network.di.Backend
import com.project.momentum.network.s3.PostDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
) {

    suspend fun getMyPosts(): List<PostDTO> {
        return try {
            val response = client.post("get-my-media") {
                header(HttpHeaders.Authorization, sessionManager.getHeader())
            }
            if (response.status == HttpStatusCode.OK) {
                Log.d("PostsAPI", "My posts: ${response.body<List<PostDTO>>()}")
                response.body<List<PostDTO>>()
            } else {
                Log.e("PostsAPI", "Error getting my posts: ${response.status}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PostsAPI", "Network error in getMyPosts ${e.message ?: ""}", e)
            emptyList()
        }
    }

    suspend fun getMyFriendsPosts(): List<PostDTO> {
        return try {
            val response = client.post("get-friends-media") {
                header(HttpHeaders.Authorization, sessionManager.getHeader())
            }

            if (response.status == HttpStatusCode.OK) {
                Log.d("PostsAPI", "Friends posts: ${response.body<List<PostDTO>>()}")
                response.body<List<PostDTO>>()
            } else {
                Log.e("PostsAPI", "Error getting friends posts: ${response.status}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PostsAPI", "Network error in getMyFriendsPosts ${e.message ?: ""}", e)
            emptyList()
        }
    }

    suspend fun sendReaction(postId: String, reaction: ReactionType) : Boolean =
        try {
            val response = client.post("react/${postId}/${reaction}") {
                header(HttpHeaders.Authorization, sessionManager.getHeader())
            }
            if (response.status == HttpStatusCode.OK) {
                Log.d("PostsAPI", "Reaction sent: ${response.body<String>()}")
                true
            } else {
                Log.e("PostsAPI", "Error sending reaction: ${response.status}")
                false
            }
        } catch (e: Exception) {
            Log.e("PostsAPI", "Network error in sendReaction ${e.message ?: ""}", e)
            false
        }
}