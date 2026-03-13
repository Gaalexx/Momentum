package com.project.momentum.features.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.account.usecases.GetMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MediaState(
    var posts: List<PostData>
)

sealed interface AccountMediaEvent {
    data object LoadPhotos : AccountMediaEvent
}

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getMediaUseCase: GetMediaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MediaState>(MediaState(listOf()))
    val state = _state.asStateFlow()

    init {
        loadPosts()
    }

    fun onEvent(event: AccountMediaEvent) {
        when (event) {
            is AccountMediaEvent.LoadPhotos -> loadPosts()
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                posts = getMediaUseCase.getAllPosts()
            )
        }
    }


}