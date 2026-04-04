package com.project.momentum.features.friends.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.project.momentum.R
import com.project.momentum.features.account.ui.AccountScreen
import com.project.momentum.features.account.viewmodel.AccountInfoState
import com.project.momentum.features.account.viewmodel.MediaState
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent

@Composable
fun FriendAccountRoot(
    friend: User,
    onEvent: (FriendsScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    userStatus: String = stringResource(R.string.account_online_status),
    onPostClick: (Int, String) -> Unit,
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onAddPostClick: () -> Unit,
) {
    AccountScreen(
        uiInfoState = AccountInfoState(friend.name, friend.avatarUrl),
        uiMediaState = MediaState(listOf()),
        onPostClick = onPostClick,
        onBackClick = onBackClick,
        onAddPostClick = {},
        userStatus = stringResource(R.string.account_online_status)
    )
}