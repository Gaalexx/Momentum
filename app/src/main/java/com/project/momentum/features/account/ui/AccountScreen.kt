@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.features.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.momentum.ui.theme.ConstColours
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.project.momentum.R
import com.project.momentum.ui.assets.S3PhotoGrid
import com.project.momentum.ui.assets.BackCircleButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.project.momentum.features.account.viewmodel.AccountInfoEvent
import com.project.momentum.features.account.viewmodel.AccountInfoState
import com.project.momentum.features.account.viewmodel.AccountInfoViewModel
import com.project.momentum.features.account.viewmodel.AccountMediaViewModel
import com.project.momentum.features.account.viewmodel.MediaState
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.ui.assets.EditCircleButton
import com.project.momentum.ui.theme.AppTextStyles


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
    accountInfoViewModel: AccountInfoViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        accountInfoViewModel.onEvent(AccountInfoEvent.GetInfo)
    }

    val uiInfoState by accountInfoViewModel.state.collectAsStateWithLifecycle()
    val posts by postsViewModel.getUserPostsFlow(uiInfoState.userId) // TODO: брать посты по id пользователя
        .collectAsStateWithLifecycle()

    AccountScreen(
        uiInfoState = uiInfoState,
        uiMediaState = MediaState(posts),
        modifier = modifier,
        userStatus = userStatus,
        onBackClick = onBackClick,
        onAddPostClick = onAddPostClick,
        onPostClick = { postId -> onPostClick(postId, uiInfoState.userId) },
        onEditClick = { onEditClick(uiInfoState) },
    )
}

@Composable
fun AccountScreen(
    uiInfoState: AccountInfoState,
    uiMediaState: MediaState,
    onPostClick: (Int) -> Unit,
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
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
                animatedVisibilityScope = animatedVisibilityScope
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
                posts = uiMediaState.posts,
                onPostClick = onPostClick,
                onAddPhotoClick = onAddPostClick ?: {},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showPlusButton = onAddPostClick != null,
                columns = 3
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
            uiMediaState = MediaState(listOf())
        )
    }
}
