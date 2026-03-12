package com.project.momentum.features.account.api

import com.project.momentum.network.di.Backend
import com.project.momentum.network.s3.PostDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import javax.inject.Inject
import javax.inject.Singleton

interface IGetAccountMediaClient {
    suspend fun getAllPosts(): List<PostDTO>
}

@Singleton
class GetAccountMediaAPI @Inject constructor(
    @Backend private val client: HttpClient
) : IGetAccountMediaClient {

    override suspend fun getAllPosts(): List<PostDTO> =
        client.post("/get-my-media").body<List<PostDTO>>()

}