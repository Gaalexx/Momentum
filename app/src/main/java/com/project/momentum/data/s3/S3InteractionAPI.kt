package com.project.momentum.data.s3

import android.net.Uri
import com.project.momentum.di.Backend
import com.project.momentum.di.S3
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import javax.inject.Named
import com.project.momentum.data.s3.UploadInfoDTO
import javax.inject.Singleton

data class PostInformation(
    val uri: Uri,
    val mimeType: String,
    val mediaType: MediaType,
    val size: Long,
    val durationMs: Long? = null,
    val label: String? = null
)

@Singleton
class S3InteractionAPI @Inject constructor(
    private val client: UploadMediaClient,
    private val s3Client: S3Client
) {

    suspend fun sendPhoto(postInfo: PostInformation) {
        val presignedURLDTO: PresignedURLDTO =
            client.sendUploadRequest(
                UploadInfoDTO(
                    postInfo.mimeType,
                    postInfo.mediaType,
                    postInfo.size,
                    postInfo.durationMs
                )
            )

        s3Client.sendFileToS3(
            presignedURLDTO.urlToLoad,
            postInfo.uri,
            postInfo.mimeType,
            postInfo.size
        )

        client.sendMessageOnComplete(S3UpdateStatusDTO(UploadingStatus.READY, postInfo.label))
    }

}
