@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.features.posts.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.R
import com.project.momentum.features.account.models.PostData
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton
import com.project.momentum.features.posts.viewmodel.GalleryEvent
import com.project.momentum.features.posts.viewmodel.PostsState
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.ui.assets.DialogEventButton
import com.project.momentum.ui.assets.S3PhotoGrid

data class PostActions(
    val onHidePost: () -> Unit = {},
    val onDeletePost: () -> Unit = {},
)

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    onPostClick: (Int) -> Unit,
    onAddPhoto: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    GalleryScreenContent(
        modifier = modifier,
        uiState = uiState,
        onRefresh = { viewModel.onEvent(GalleryEvent.OnRefreshPosts) },
        onPostClick = onPostClick,
        onLongPostClick = { post ->
            viewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
            viewModel.onEvent(GalleryEvent.SelectPost(post))
        },
        postActions = PostActions(
            onHidePost = {
                viewModel.onEvent(GalleryEvent.OnHidePost)
                viewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                viewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            onDeletePost = {
                viewModel.onEvent(GalleryEvent.OnDeletePost)
                viewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                viewModel.onEvent(GalleryEvent.SelectPost(null))
            }
        ),
        onAddPhoto = onAddPhoto,
        onProfileClick = onProfileClick,
        onBackClick = onBackClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
private fun GalleryScreenContent(
    modifier: Modifier = Modifier,
    uiState: PostsState,
    onRefresh: () -> Unit,
    onPostClick: (Int) -> Unit,
    onLongPostClick: (Int?) -> Unit,
    postActions: PostActions,
    onAddPhoto: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val bg = ConstColours.BLACK
    val textColor = Color.White

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackCircleButton(onClick = onBackClick)

                Spacer(Modifier.width(12.dp))

                ProfileCircleButton(onClick = onProfileClick)

                Spacer(Modifier.weight(1f))

                FriendsPillButton(onClick = onGoToFriends)

                Spacer(Modifier.weight(1f))

                SettingsCircleButton(onClick = onGoToSettings)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.gallery_title),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh
            ) {
                S3PhotoGrid(
                    posts = uiState.posts,
                    onPostClick = onPostClick,
                    onLongPostClick = onLongPostClick,
                    onAddPhotoClick = {},
                    modifier = Modifier
                        .fillMaxSize(),
                    showPlusButton = false,
                    columns = 3,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }

    if (uiState.isShowingActionsDialog) {
        Dialog(
            onDismissRequest = { onLongPostClick(null) }
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .background(ConstColours.MAIN_BACK_GRAY)
            ) {
                DialogEventButton(
                    text = R.string.button_hide_post_for_me,
                    icon = Icons.Outlined.HideImage,
                    onClick = postActions.onHidePost
                )
                if (uiState.posts[uiState.selectedPost ?: throw Exception("post not selected")].isOwner) {
                    DialogEventButton(
                        text = R.string.button_delete_post,
                        icon = Icons.Outlined.Delete,
                        onClick = postActions.onDeletePost,
                        textColor = ConstColours.DELETE
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun GalleryScreenPreview() {
    MaterialTheme {
        GalleryScreenContent(
            uiState = PostsState(listOf(), false, "0", isShowingActionsDialog = true),
            onRefresh = {},
            onPostClick = {},
            onLongPostClick = {},
            postActions = PostActions(),
            onProfileClick = {},
            onBackClick = {},
            onGoToSettings = {},
            onGoToFriends = {}
        )
    }
}
