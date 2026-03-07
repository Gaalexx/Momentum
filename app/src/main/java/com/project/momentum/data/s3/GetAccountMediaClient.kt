package com.project.momentum.data.s3

import com.project.momentum.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import javax.inject.Inject
import javax.inject.Singleton

interface IGetAccountMediaClient {
    suspend fun getAllPosts(): List<PostDTO>
}

@Singleton
class GetAccountMediaClient @Inject constructor(
    @Backend private val client: HttpClient
) : IGetAccountMediaClient {

    override suspend fun getAllPosts(): List<PostDTO> =
        client.post("/get-my-media").body<List<PostDTO>>()

}