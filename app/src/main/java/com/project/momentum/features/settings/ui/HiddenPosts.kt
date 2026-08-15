package com.project.momentum.features.settings.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.R
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.viewmodel.GalleryEvent
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.ui.assets.DialogInfo
import com.project.momentum.ui.assets.S3PhotoGrid
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.theme.ConstColours

@Composable
fun HiddenPosts(
    onPostClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    postsViewModel: PostsViewModel = hiltViewModel()
) {
    val uiState by postsViewModel.state.collectAsStateWithLifecycle()
    val posts by postsViewModel.getHiddenPostsFlow().collectAsStateWithLifecycle()

    HiddenPostsScreenContent(
        posts = posts,
        isRefreshing = uiState.isRefreshing,
        onRefresh = { postsViewModel.onEvent(GalleryEvent.OnRefreshPosts) },
        onPostClick = onPostClick,
        onLongPostClick = { post ->
            postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
            postsViewModel.onEvent(GalleryEvent.SelectPost(post))
        },
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
        onBackClick = onBackClick,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
private fun HiddenPostsScreenContent(
    modifier: Modifier = Modifier,
    posts: List<PostData>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onPostClick: (Int) -> Unit,
    onLongPostClick: (String?) -> Unit,
    postDialogInfo: DialogInfo.Hidden,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val bg = ConstColours.BLACK
    val textColor = Color.White

    TopBarTemplate(
        label = R.string.settings_hidden_posts,
        onBackClick = onBackClick,
        modifier = modifier
    ) {
        Column() {
            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.settings_hidden_posts),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ) {
                S3PhotoGrid(
                    posts = posts,
                    onPostClick = onPostClick,
                    onLongPostClick = onLongPostClick,
                    postDialogInfo = postDialogInfo,
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
}

@Preview
@Composable
fun HiddenPostsScreenContentPreview() {
    HiddenPostsScreenContent(
        posts = listOf(),
        isRefreshing = false,
        onRefresh = {},
        onPostClick = {},
        onLongPostClick = {},
        postDialogInfo = DialogInfo.Hidden(),
        onBackClick = {}
    )
}