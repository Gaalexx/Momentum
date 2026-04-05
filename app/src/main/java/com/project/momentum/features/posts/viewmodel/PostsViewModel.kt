package com.project.momentum.features.posts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.repo.PostsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class PostsState(
    val posts: List<PostData>,
    val isRefreshing: Boolean
)

sealed interface GalleryEvent {
    data object OnLoadPosts : GalleryEvent
    data object OnRefreshPosts : GalleryEvent
    data class OnLocalLoadPosts(val posts: List<PostData>) : GalleryEvent
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repo: PostsRepo
) : ViewModel() {
    private val _state = MutableStateFlow<PostsState>(PostsState(listOf(), false))
    val state = _state.asStateFlow()

    init {
        loadAllPosts()
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.OnLoadPosts -> loadAllPosts()
            is GalleryEvent.OnRefreshPosts -> refreshPosts()
            is GalleryEvent.OnLocalLoadPosts -> loadLocalPosts(event)
            else -> println()
        }
    }

    private suspend fun postsUpdate() {
        val posts = repo.getAllPosts()
        _state.update {
            it.copy(posts = posts)
        }
    }

    private fun loadLocalPosts(event: GalleryEvent.OnLocalLoadPosts){
        viewModelScope.launch {
            _state.update {
                it.copy(posts = event.posts)
            }
        }
    }

    private fun loadAllPosts() {
        viewModelScope.launch {
            postsUpdate()
        }
    }

    private fun refreshPosts() {
        viewModelScope.launch {
            _state.update {
                it.copy(isRefreshing = true)
            }
            postsUpdate()
            _state.update {
                it.copy(isRefreshing = false)
            }
        }
    }

    fun getUserPostsFlow(userName: String): StateFlow<List<PostData>> {
        return state.map { s ->
            s.posts
                .filter { it.userName == userName }
                .sortedByDescending { it.createdAtInstantOrNull() ?: Instant.MIN }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )
    }
}
