package com.project.momentum.features.posts.api

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
        val response = client.post("get-my-media") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }
        if (response.status == HttpStatusCode.OK) {
            return response.body<List<PostDTO>>()
        } else {
            throw Exception()
        }
    }

    suspend fun getMyFriendsPosts(): List<PostDTO> {
        val response = client.post("get-friends-media") {
            header(HttpHeaders.Authorization, sessionManager.getHeader())
        }

        if (response.status == HttpStatusCode.OK) {
            return response.body<List<PostDTO>>()
        } else {
            throw Exception()
        }
    }

}