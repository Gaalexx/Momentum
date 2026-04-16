package com.project.momentum.features.posts.api

import android.util.Log
import com.project.momentum.data.auth.SessionManager
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
                response.body<List<PostDTO>>()
            } else {
                Log.e("PostsAPI", "Error getting my posts: ${response.status}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PostsAPI", "Network error in getMyPosts", e)
            emptyList()
        }
    }

    suspend fun getMyFriendsPosts(): List<PostDTO> {
        return try {
            val response = client.post("get-friends-media") {
                header(HttpHeaders.Authorization, sessionManager.getHeader())
            }

            if (response.status == HttpStatusCode.OK) {
                response.body<List<PostDTO>>()
            } else {
                Log.e("PostsAPI", "Error getting friends posts: ${response.status}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PostsAPI", "Network error in getMyFriendsPosts", e)
            emptyList()
        }
    }

}