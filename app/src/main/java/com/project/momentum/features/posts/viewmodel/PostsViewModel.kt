package com.project.momentum.features.posts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.repo.PostsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.listOf

data class PostsState(
    val posts: List<PostData>
)

sealed interface GalleryEvent {
    data object OnLoadPosts : GalleryEvent
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repo: PostsRepo
) : ViewModel() {
    private val _state = MutableStateFlow<PostsState>(PostsState(listOf()))
    val state = _state.asStateFlow()

    init {
        loadAllPosts()
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.OnLoadPosts -> loadAllPosts()
            else -> println()
        }
    }

    private fun loadAllPosts() {
        viewModelScope.launch {
            val posts = repo.getAllPosts()
            _state.update {
                it.copy(posts = posts)
            }
        }
    }

}