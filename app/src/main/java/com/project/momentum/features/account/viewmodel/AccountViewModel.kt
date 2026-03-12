package com.project.momentum.features.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.repo.AccountRepository
import com.project.momentum.network.s3.PostDTO
import com.project.momentum.network.s3.PostInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AccountState {
    data object Idle : AccountState
    data object Loading : AccountState
    data object Done : AccountState
}

sealed interface AccountEvent {
    data class Send(val postInfo: PostInformation) : AccountEvent
    //data object Retry : UploadEvent
    //data object Reset : UploadEvent
}

@HiltViewModel
class AccountViewModel @Inject constructor( // TODO прописать состояния
    private val repository: AccountRepository
) : ViewModel() {

//    private val _state = MutableStateFlow<AccountState>(AccountState.Idle)
//    val state = _state.asStateFlow()

    private val _posts = MutableStateFlow<List<PostDTO>>(emptyList())
    val posts = _posts.asStateFlow()

    fun loadPosts() = viewModelScope.launch {
        _posts.value = repository.getAllPosts()
    }


//    fun onEvent(event: AccountEvent) {
//        when (event) {
//            is UploadEvent.Send -> loadPosts()
//            else -> {}
//        }
//    }
//
//    private fun getPosts(): List<PostDTO> {
//        return viewModelScope.launch {
//            repository.getAllPosts()
//        } as List<PostDTO>
//    }

}