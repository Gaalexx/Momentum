package com.project.momentum.features.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.momentum.data.auth.SessionManager
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.features.reactions.models.ReactionData
import com.project.momentum.features.posts.features.reactions.models.ReactionType
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
    val isRefreshing: Boolean,
    val isShowingReactionsDialog: Boolean = false
)

sealed interface GalleryEvent {
    data object OnLoadPosts : GalleryEvent
    data object OnRefreshPosts : GalleryEvent
    data class OnLocalLoadPosts(val posts: List<PostData>) : GalleryEvent
}

sealed interface WatchPhotoEvent {
    data class OnShowReactionDialogEvent(val isShowing: Boolean) : WatchPhotoEvent

    data class OnSendReaction(
        val postId: String,
        val reaction: ReactionType,
    ) : WatchPhotoEvent
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repo: PostsRepo,
    private val sessionManager: SessionManager
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

    fun onWatchPhotoEvent(event: WatchPhotoEvent) {
        when (event) {
            is WatchPhotoEvent.OnShowReactionDialogEvent -> showReactionDialogChange(event)
            is WatchPhotoEvent.OnSendReaction -> sendReaction(event)
        }
    }

    private fun showReactionDialogChange(event: WatchPhotoEvent.OnShowReactionDialogEvent) {
        _state.update {
            it.copy(isShowingReactionsDialog = event.isShowing)
        }
    }

    private fun sendReaction(event: WatchPhotoEvent.OnSendReaction) {
        val currentUserId = sessionManager.getUserId() ?: return

        if (_state.value.posts.find { it.id == event.postId }
                ?.reactions?.find { it.emoji == event.reaction }
                ?.users?.any { it == currentUserId } ?: false) return //TODO deleteReaction()

        val oldState = _state.value

        _state.update { state ->
            state.copy(
                posts = state.posts.map { post ->
                    if (post.id == event.postId) {
                        if ((post.reactions ?: listOf()).any { it.emoji == event.reaction }) {
                            post.copy(
                                reactions = (post.reactions ?: listOf()).map { reaction ->
                                    if (reaction.emoji == event.reaction) {
                                        reaction.copy(
                                            count = reaction.count + 1,
                                            users = reaction.users + listOf(currentUserId)
                                        )
                                    } else reaction
                                }
                            )
                        } else {
                            post.copy(
                                reactions = (post.reactions ?: listOf()) + listOf(
                                    ReactionData(
                                        event.reaction,
                                        count = 1,
                                        users = listOf(currentUserId)
                                    )
                                )
                            )
                        }

                    } else post
                }
            )
        }
        viewModelScope.launch {
            try {
                if (!repo.sendReaction(event.postId, event.reaction)) {
                    _state.update { oldState }
                }
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error sending reaction ${e.message ?: ""}", e)
                _state.update { oldState }
            }
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

    fun getUserPostsFlow(userId: String): StateFlow<List<PostData>> {
        return state.map { s ->
            s.posts
                .filter { it.userId == userId }
                .sortedByDescending { it.createdAtInstantOrNull() ?: Instant.MIN }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )
    }
}
