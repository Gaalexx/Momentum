package com.project.momentum.domain.media.repository

import com.project.momentum.domain.media.model.AvatarInfo
import com.project.momentum.domain.media.model.PostInformation

interface MediaUploadRepository {
    suspend fun sendContent(
        postInfo: PostInformation,
        onProgress: (Long, Long?) -> Unit = { _, _ -> }
    )

    suspend fun sendAvatar(avatarInfo: AvatarInfo)

    suspend fun discardContent(uri: String)
}
