package com.project.momentum.network.s3

import android.net.Uri
import android.util.Log
import com.project.momentum.features.editingAccount.AvatarInfo
import com.project.momentum.network.s3.upload.UploadMediaAPI
import com.project.momentum.network.s3.upload.S3UploadApi
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
    private val client: UploadMediaAPI,
    private val s3UploadAPI: S3UploadApi
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


        s3UploadAPI.sendFileToS3(
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

    suspend fun sendAvatar(avatarInfo: AvatarInfo) {
        val presignedURLDTO: PresignedURLDTO = client
            .sendAvatarUploadRequest (
                UploadAvatarInfoDTO(
                    avatarInfo.mimeType,
                    avatarInfo.size
                )
            )
        try {
            s3UploadAPI.sendFileToS3(
                presignedURLDTO.urlToLoad,
                avatarInfo.uri,
                avatarInfo.mimeType,
                avatarInfo.size
            )

            val body =
                client.sendAvatarUploadingStatus(    // TODO в зависимости от того какой ответ, то и будем отображать на экране
                    S3UpdateStatusDTO(
                        UploadingStatus.READY,
                        presignedURLDTO.mediaId,
                        //title
                    )
                )
        } catch (e: Exception) {
            //TODO: сообщить юзеру об ошибке
            val body =
                client.sendAvatarUploadingStatus(    // TODO в зависимости от того какой ответ, то и будем отображать на экране
                    S3UpdateStatusDTO(
                        UploadingStatus.FAILED,
                        presignedURLDTO.mediaId,
                        //title
                    )
                )
            Log.e("Momentum", e.message.toString())
        }
    }
}
