package com.project.momentum.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.data.s3.PostInformation
import com.project.momentum.data.s3.S3InteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UploadState {
    data object Idle : UploadState
    data class Uploading(val progress: Int? = null) : UploadState
    data class Success(val mediaId: String? = null) : UploadState
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

    private val _state = MutableStateFlow<UploadState>(UploadState.Idle)
    val state = _state.asStateFlow()

    fun onEvent(event: UploadEvent) {
        when (event) {
            is UploadEvent.Send -> upload(event.postInfo)
        }
    }

    private fun upload(postInfo: PostInformation) {
        viewModelScope.launch {
            // runCatching {
            uploaderRepo.sendPhoto(postInfo)
//            }.onSuccess {
//                _state.value = UploadState.Success()
//                // TODO реализовать случай успеха
//            }/*.onFailure {
//                _state.value = UploadState.Uploading()
//                // TODO реализовать случай неудачи :(
//            }*/
        }
    }

}
