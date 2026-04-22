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
    val currentUserId: String,
    val isShowingReactionsDialog: Boolean = false
)

sealed interface GalleryEvent {
    data object OnLoadPosts : GalleryEvent
    data object OnRefreshPosts : GalleryEvent
    data class OnLocalLoadPosts(val posts: List<PostData>) : GalleryEvent
}

sealed interface WatchPhotoEvent {
    data class OnShowReactionDialogEvent(val isShowing: Boolean) : WatchPhotoEvent

    data class OnReactionClick(
        val postId: String,
        val reaction: ReactionType,
    ) : WatchPhotoEvent
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repo: PostsRepo,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow<PostsState>(PostsState(listOf(), false, ""))
    val state = _state.asStateFlow()

    init {
        loadAllPosts()
        _state.update {
            it.copy(currentUserId = getCurrentUserId())
        }
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
            is WatchPhotoEvent.OnReactionClick -> try {
                onReactionClick(event)
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error on reaction click ${e.message ?: ""}", e)
                //TODO: notice user about error
            }
        }
    }

    private fun showReactionDialogChange(event: WatchPhotoEvent.OnShowReactionDialogEvent) {
        _state.update {
            it.copy(isShowingReactionsDialog = event.isShowing)
        }
    }

    private fun onReactionClick(event: WatchPhotoEvent.OnReactionClick) {
        val currentUserId = sessionManager.getUserId() ?: throw Exception() // TODO: custom Exception

        val post = _state.value.posts.find { it.id == event.postId } ?: throw Exception() // no such post


        //TODO: если часто добавлять или удалять реакции и проблемы с сетью
        // то отображается некорректное состояние например:
        // удалились реакции на клиенте но на сервере всё ещё ошибка
        // и при обновлении постов будет несоответствие состояний
        // (из ниоткуда появятся вроде как удаленные реакции)

        if (post.reactions?.find { it.emoji == event.reaction }
                ?.users?.any { it == currentUserId } ?: false) {
            deleteReaction(currentUserId, event.postId, event.reaction)
        } else {
            sendReaction(currentUserId, event.postId, event.reaction)
        }
    }

    private fun deleteReaction(userId: String, postId: String, currentReaction: ReactionType) {
        val oldState = _state.value

        _state.update { state ->
            state.copy(
                posts = state.posts.map { post ->
                    if (post.id == postId) {
                        if (post.reactions?.any { it.emoji == currentReaction } ?: false) {
                            post.copy(
                                reactions = post.reactions.mapNotNull { reaction ->
                                    if (reaction.emoji == currentReaction) {
                                        if (reaction.users.size > 1) {
                                            reaction.copy(
                                                users = reaction.users - userId
                                            )
                                        } else null
                                    } else reaction
                                }
                            )
                        } else throw Exception() // no such reaction or reactions == null
                    } else post
                }
            )
        }
        viewModelScope.launch {
            try {
                if (!repo.deleteReaction(postId, currentReaction)) {
                    _state.update { oldState }
                }
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error deleting reaction ${e.message ?: ""}", e)
                _state.update { oldState }
            }
        }
    }

    private fun sendReaction(userId: String, postId: String, currentReaction: ReactionType) {
        val oldState = _state.value

        _state.update { state ->
            state.copy(
                posts = state.posts.map { post ->
                    if (post.id == postId) {
                        if ((post.reactions ?: listOf()).any { it.emoji == currentReaction }) {
                            post.copy(
                                reactions = (post.reactions ?: listOf()).map { reaction ->
                                    if (reaction.emoji == currentReaction) {
                                        reaction.copy(
                                            users = reaction.users + listOf(userId)
                                        )
                                    } else reaction
                                }
                            )
                        } else {
                            post.copy(
                                reactions = (post.reactions ?: listOf()) + listOf(
                                    ReactionData(
                                        currentReaction,
                                        users = listOf(userId)
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
                if (!repo.sendReaction(postId, currentReaction)) {
                    _state.update { oldState }
                }
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error sending reaction ${e.message ?: ""}", e)
                _state.update { oldState }
            }
        }
    }

    private fun getCurrentUserId(): String = sessionManager.getUserId() ?: throw Exception("Unauthenticated")

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
