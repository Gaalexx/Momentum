package com.project.momentum.features.contentcreation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.network.s3.PostInformation
import com.project.momentum.network.s3.S3InteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UploadState {
    data object Idle : UploadState
    data class Uploading(val progress: Int? = null) : UploadState
    data class Success(val mediaId: String? = null) : UploadState
    data object Error : UploadState
}

sealed interface UploadEvent {
    data class Send(val postInfo: PostInformation) : UploadEvent
    data class ToggleFriend(val friendId: String) : UploadEvent
    data class SyncFriends(val friendIds: List<String>) : UploadEvent
}

@HiltViewModel
class ContentCreationViewModel @Inject constructor(
    private val uploaderRepo: S3InteractionRepository
) : ViewModel() {
    private companion object {
        const val TAG = "ContentUpload"
    }

    private val _state = MutableStateFlow<UploadState>(UploadState.Idle)
    val state = _state.asStateFlow()

    private val _selectedFriendIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedFriendIds = _selectedFriendIds.asStateFlow()

    fun onEvent(event: UploadEvent) {
        when (event) {
            is UploadEvent.Send -> {
                if (_state.value is UploadState.Idle) {
                    upload(event.postInfo)
                }
            }

            is UploadEvent.ToggleFriend -> {
                _selectedFriendIds.value = if (_selectedFriendIds.value.contains(event.friendId)) {
                    _selectedFriendIds.value - event.friendId
                } else {
                    _selectedFriendIds.value + event.friendId
                }
            }

            is UploadEvent.SyncFriends -> {
                _selectedFriendIds.value = event.friendIds.toSet()
            }
        }
    }

    private fun calculateUploadPercent(
        sentBytes: Long,
        totalBytes: Long?
    ): Int? {
        val total = totalBytes?.takeIf { it > 0L } ?: return null

        return ((sentBytes * 100L) / total)
            .toInt()
            .coerceIn(0, 95)
    }

    private fun upload(postInfo: PostInformation) {
        viewModelScope.launch {
            val receiverIds = _selectedFriendIds.value.toList()
            runCatching {
                _state.value = UploadState.Uploading(progress = 0)
                uploaderRepo.sendContent(postInfo.copy(receiverIds = receiverIds)) { progress, total ->
                    _state.value =
                        UploadState.Uploading(progress = calculateUploadPercent(progress, total))
                }
                _state.value = UploadState.Uploading(progress = 100)
            }.onSuccess {
                _state.value = UploadState.Success()
                delay(200)
                _state.value = UploadState.Idle
                // TODO реализовать случай успеха
            }.onFailure { throwable ->
                Log.e(
                    TAG,
                    "Failed to upload content: uri=${postInfo.uri}, mimeType=${postInfo.mimeType}, " +
                            "mediaType=${postInfo.mediaType}, size=${postInfo.size}",
                    throwable
                )
                _state.value = UploadState.Error
                //_state.value = UploadState.Uploading()
                // TODO реализовать случай неудачи :(
            }
        }
    }
}
