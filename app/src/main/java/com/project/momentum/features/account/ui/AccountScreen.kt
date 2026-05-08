@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.features.account.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.account.viewmodel.AccountInfoEvent
import com.project.momentum.features.account.viewmodel.AccountInfoState
import com.project.momentum.features.account.viewmodel.AccountInfoViewModel
import com.project.momentum.features.posts.viewmodel.GalleryEvent
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.EditCircleButton
import com.project.momentum.ui.assets.PostDialogInfo
import com.project.momentum.ui.assets.S3PhotoGrid
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours


@Composable
fun AccountRoot(
    onBackClick: () -> Unit,
    onAddPostClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (AccountInfoState) -> Unit = {},
    onPostClick: (Int, String) -> Unit,
    onProfileClick: () -> Unit = {},
    userStatus: String = stringResource(R.string.account_online_status),
    postsViewModel: PostsViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    accountInfoViewModel: AccountInfoViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        accountInfoViewModel.onEvent(AccountInfoEvent.GetInfo)
    }

    val uiInfoState by accountInfoViewModel.state.collectAsStateWithLifecycle()
    val uiState by postsViewModel.state.collectAsStateWithLifecycle()
    val posts by postsViewModel.getUserPostsFlow(uiInfoState.userId)
        .collectAsStateWithLifecycle()

    AccountScreen(
        uiInfoState = uiInfoState,
        posts = posts,
        modifier = modifier,
        userStatus = userStatus,
        onBackClick = onBackClick,
        onAddPostClick = onAddPostClick,
        onPostClick = { postId -> onPostClick(postId, uiInfoState.userId) },
        onLongPostClick = { post ->
            postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
            postsViewModel.onEvent(GalleryEvent.SelectPost(post))
        },
        postDialogInfo = PostDialogInfo(
            onHidePost = {
                postsViewModel.onEvent(GalleryEvent.OnHidePost)
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            onDeletePost = {
                postsViewModel.onEvent(GalleryEvent.OnDeletePost)
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            isShowingActionsDialog = uiState.isShowingActionsDialog,
            selectedPost = uiState.selectedPost
        ),
        onEditClick = { onEditClick(uiInfoState) },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
fun AccountScreen(
    uiInfoState: AccountInfoState,
    posts: List<PostData>,
    onPostClick: (Int) -> Unit,
    onLongPostClick: (String?) -> Unit,
    postDialogInfo: PostDialogInfo,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onAddPostClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
    userStatus: String = stringResource(R.string.account_online_status),
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = Color(0xFFEDEEF2) // TODO: fa fa fa what a faaa
    val textColor = ConstColours.WHITE
    val context: Context = LocalContext.current


    val avatarModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(
                    key = "person-avatar-${uiInfoState.userId}"
                ),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ ->
                    tween(750)
                }
            )
        }
    } else {
        Modifier
    }

    val nameModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    key = "person-name-${uiInfoState.userId}"
                ),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ ->
                    tween(750)
                },
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
            )
        }
    } else {
        Modifier
    }

    //TODO: экран загрузки (ну и состояние)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.medium_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackCircleButton(
                onClick = onBackClick
            )
            Spacer(Modifier.weight(1f))

            if (onEditClick != null) {
                EditCircleButton(
                    onClick = onEditClick,
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = avatarModifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(chrome2)
                    .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
            ) {
                if (uiInfoState.profilePhotoURL == null) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.account_avatar),
                        tint = iconTint.copy(alpha = 0.7f),
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                    )
                } else {
                    AsyncImage(
                        model = uiInfoState.profilePhotoURL,
                        contentDescription = stringResource(R.string.account_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.medium_padding)))

            Text(
                text = uiInfoState.name,
                style = AppTextStyles.MainText.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = nameModifier
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.extra_small_padding)))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (userStatus == stringResource(R.string.account_online_status)) {
                    Box(                         // пока у нас не определяется статус онлайн на бэке, будет такое :)
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                Spacer(Modifier.width(dimensionResource(R.dimen.small_padding)))
                Text(
                    text = userStatus,
                    color = Color(0xFFA0A0A0), // TODO: fa fa fa what a faaa
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(chrome2.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.account_my_publications),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )

            S3PhotoGrid(
                posts = posts,
                onPostClick = onPostClick,
                onLongPostClick = onLongPostClick,
                postDialogInfo = postDialogInfo,
                onAddPhotoClick = onAddPostClick ?: {},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showPlusButton = onAddPostClick != null,
                columns = 3,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun AccountScreenPreview() {
    MaterialTheme {
        AccountScreen(
            onPostClick = {},
            onLongPostClick = {},
            postDialogInfo = PostDialogInfo(),
            onEditClick = {},
            onBackClick = {},
            onAddPostClick = {},
            uiInfoState = AccountInfoState(
                "52",
                "Preview",
                "Preview",
                null,
                null,
                false
            ),
            posts = listOf()
        )
    }
}
