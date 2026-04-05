package com.project.momentum.features.friends.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.R
import com.project.momentum.features.account.ui.AccountScreen
import com.project.momentum.features.account.viewmodel.AccountInfoState
import com.project.momentum.features.account.viewmodel.MediaState
import com.project.momentum.features.posts.viewmodel.PostsViewModel

@Composable
fun FriendAccountRoot(
    friend: User,
    modifier: Modifier = Modifier,
    onPostClick: (Int, String) -> Unit,
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    postsViewModel: PostsViewModel = hiltViewModel()
) {
    val friendPosts by postsViewModel.getUserPostsFlow(friend.name).collectAsStateWithLifecycle()

    AccountScreen(
        uiInfoState = AccountInfoState(friend.name, friend.avatarUrl),
        uiMediaState = MediaState(friendPosts),
        onPostClick = { postIndex -> onPostClick(postIndex, friend.name) },
        onBackClick = onBackClick,
        modifier = modifier,
        userStatus =
            if (friend.isOnline) stringResource(R.string.account_online_status)
            else stringResource(R.string.account_offline_status),
    )
}