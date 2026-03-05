package com.project.momentum.data.s3

import com.project.momentum.di.Backend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


interface IUploadMediaClient {
    suspend fun sendUploadRequest(mediaInfo: UploadInfoDTO): PresignedURLDTO
    suspend fun sendMessageOnComplete(statusInfo: S3UpdateStatusDTO)
}


@Singleton
class UploadMediaClient @Inject constructor(
    @Backend private val client: HttpClient,
) : IUploadMediaClient {
    override suspend fun sendUploadRequest(mediaInfo: UploadInfoDTO): PresignedURLDTO {
        val response: PresignedURLDTO = client.post("/upload") {
            setBody(mediaInfo)
        }.body<PresignedURLDTO>()

        return response
    }

    override suspend fun sendMessageOnComplete(statusInfo: S3UpdateStatusDTO) {
        val response = client.post("/status-upload") {
            setBody(statusInfo)
        }.body<HttpStatusCode>()
    }
}
