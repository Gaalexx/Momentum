package com.project.momentum.data.s3

import android.net.Uri
import javax.inject.Inject
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
class S3InteractionRepository @Inject constructor(
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

        val body =
            client.sendMessageOnComplete(    // TODO в зависимости от того какой ответ, то и будем отображать на экране
                S3UpdateStatusDTO(
                    UploadingStatus.READY,
                    presignedURLDTO.mediaId,
                    postInfo.label
                )
            )
    }

}
