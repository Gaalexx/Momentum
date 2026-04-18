package com.project.momentum.features.contentcreation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.contentcreation.ui.deleteByUri
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
    //data object Retry : UploadEvent
    //data object Reset : UploadEvent
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

    fun onEvent(event: UploadEvent) {
        when (event) {
            is UploadEvent.Send -> {
                if (_state.value is UploadState.Idle) {
                    upload(event.postInfo)
                }
            }

            else -> {}
        }
    }

    private fun upload(postInfo: PostInformation) {
        viewModelScope.launch {
            runCatching {
                _state.value = UploadState.Uploading(progress = 0)
                uploaderRepo.sendContent(postInfo) { progress ->
                    _state.value = UploadState.Uploading(progress = progress)
                }
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
