package com.project.momentum.data.media.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.project.momentum.data.media.remote.MediaTypeDto
import com.project.momentum.data.media.remote.S3UpdateStatusDTO
import com.project.momentum.data.media.remote.S3UploadApi
import com.project.momentum.data.media.remote.UploadAvatarInfoDTO
import com.project.momentum.data.media.remote.UploadInfoDTO
import com.project.momentum.data.media.remote.UploadMediaAPI
import com.project.momentum.data.media.remote.UploadingStatus
import com.project.momentum.di.IoDispatcher
import com.project.momentum.domain.media.model.AvatarInfo
import com.project.momentum.domain.media.model.PostInformation
import com.project.momentum.domain.media.repository.MediaUploadRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaUploadRepositoryImpl @Inject constructor(
    private val client: UploadMediaAPI,
    private val s3UploadAPI: S3UploadApi,
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MediaUploadRepository {

    override suspend fun sendContent(
        postInfo: PostInformation,
        onProgress: (Long, Long?) -> Unit
    ) {
        val upload = client.sendUploadRequest(
            UploadInfoDTO(
                mimeType = postInfo.mimeType,
                mediaType = MediaTypeDto.valueOf(postInfo.mediaType.name),
                size = postInfo.size,
                durationMs = postInfo.durationMs
            )
        )
        s3UploadAPI.sendFileToS3(
            upload.urlToLoad,
            Uri.parse(postInfo.uri),
            postInfo.mimeType,
            postInfo.size,
            onProgress
        )
        client.sendMessageOnComplete(
            S3UpdateStatusDTO(
                UploadingStatus.READY,
                upload.mediaId,
                postInfo.label,
                postInfo.receiverIds
            )
        )
        discardContent(postInfo.uri)
    }

    override suspend fun sendAvatar(avatarInfo: AvatarInfo) {
        val upload = client.sendAvatarUploadRequest(
            UploadAvatarInfoDTO(avatarInfo.mimeType, avatarInfo.size)
        )
        try {
            s3UploadAPI.sendFileToS3(
                upload.urlToLoad,
                Uri.parse(avatarInfo.uri),
                avatarInfo.mimeType,
                avatarInfo.size
            )
            client.sendAvatarUploadingStatus(
                S3UpdateStatusDTO(UploadingStatus.READY, upload.mediaId)
            )
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            client.sendAvatarUploadingStatus(
                S3UpdateStatusDTO(UploadingStatus.FAILED, upload.mediaId)
            )
            Log.e("Momentum", error.message.toString())
        }
    }

    override suspend fun discardContent(uri: String) {
        withContext(ioDispatcher) {
            try {
                contentResolver.delete(Uri.parse(uri), null, null)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                // Some providers disallow deletion. Upload/discard still completes as before.
            }
        }
    }
}
