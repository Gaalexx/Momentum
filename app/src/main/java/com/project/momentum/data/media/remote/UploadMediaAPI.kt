package com.project.momentum.data.media.remote

import com.project.momentum.data.auth.SessionManager
import com.project.momentum.di.Backend
import com.project.momentum.data.media.remote.PresignedURLDTO
import com.project.momentum.data.media.remote.S3UpdateStatusDTO
import com.project.momentum.data.media.remote.UploadAvatarInfoDTO
import com.project.momentum.data.media.remote.UploadInfoDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton


interface IUploadMediaAPI {
    suspend fun sendUploadRequest(mediaInfo: UploadInfoDTO): PresignedURLDTO
    suspend fun sendAvatarUploadRequest(avatarInfo: UploadAvatarInfoDTO): PresignedURLDTO
    suspend fun sendMessageOnComplete(statusInfo: S3UpdateStatusDTO)
    suspend fun sendAvatarUploadingStatus(statusInfo: S3UpdateStatusDTO)
}


@Singleton
class UploadMediaAPI @Inject constructor(
    @Backend private val client: HttpClient,
    private val sessionManager: SessionManager
) : IUploadMediaAPI {
    override suspend fun sendUploadRequest(mediaInfo: UploadInfoDTO): PresignedURLDTO {
        val response: PresignedURLDTO = client.post("upload") {
            setBody(mediaInfo)
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<PresignedURLDTO>()

        return response
    }

    override suspend fun sendAvatarUploadRequest(avatarInfo: UploadAvatarInfoDTO): PresignedURLDTO {
        val response: PresignedURLDTO = client.post("upload-avatar") {
            setBody(avatarInfo)
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<PresignedURLDTO>()

        return response
    }

    override suspend fun sendMessageOnComplete(statusInfo: S3UpdateStatusDTO) {
        val response = client.post("status-upload") {
            setBody(statusInfo)
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<HttpStatusCode>()
    }

    override suspend fun sendAvatarUploadingStatus(statusInfo: S3UpdateStatusDTO) {
        val response = client.post("status-upload-avatar") {
            setBody(statusInfo)
            header(HttpHeaders.Authorization, "Bearer ${sessionManager.getToken()}")
        }.body<HttpStatusCode>()
    }
}
