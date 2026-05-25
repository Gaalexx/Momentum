package com.project.momentum.features.posts.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.posts.viewmodel.GalleryEvent
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.features.posts.viewmodel.WatchPhotoEvent
import com.project.momentum.ui.assets.DialogInfo

@Composable
fun WatchHiddenPhotoScreenRoute(
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    postIndex: Int,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val uiState by postsViewModel.state.collectAsStateWithLifecycle()

    val hiddenPosts by postsViewModel.getHiddenPostsFlow().collectAsStateWithLifecycle()

    WatchPhotoScreenFull(
        onGoToTakePhoto = onGoToTakePhoto,
        onGoToGallery = onGoToGallery,
        onProfileClick = onProfileClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,

        postDialogInfo = DialogInfo.Hidden(
            onShowPost = {
                postsViewModel.onEvent(
                    GalleryEvent.OnShowPost(
                        uiState.selectedPost
                            ?: throw Exception("GalleryScreenContent:OnHidePost: Selected post is null")
                    )
                )
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            isShowingActionsDialog = uiState.isShowingActionsDialog,
            selectedPost = uiState.selectedPost
        ),

        onShowReactionDialog = { postId ->
            postsViewModel.onWatchPhotoEvent(
                WatchPhotoEvent.OnShowReactionDialogEvent(!uiState.isShowingActionsDialog)
            )
            postsViewModel.onEvent(GalleryEvent.SelectPost(postId))

        },
        onReactionClick = { postId, reaction ->
            postsViewModel.onWatchPhotoEvent(
                WatchPhotoEvent.OnReactionClick(postId, reaction)
            )
        },
        onTranscript = { postId ->
            postsViewModel.onEvent(GalleryEvent.GetSpeechTranscription(postId))
        },
        postIndex = postIndex,
        posts = hiddenPosts,
        uiState = uiState,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}