package com.project.momentum.features.friends.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.MomentumTheme
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.R
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent
import com.project.momentum.features.friends.viewmodel.FriendsViewModel
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.FriendSearchField
import com.project.momentum.ui.assets.AddFriendCircleButton
import com.project.momentum.features.friends.ui.assets.AddFriendDialog
import com.project.momentum.features.friends.ui.assets.DeleteFriendDialog
import com.project.momentum.features.friends.ui.assets.FriendRequestCarousel
import com.project.momentum.features.friends.viewmodel.FriendsScreenState
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt


data class Friend(
    val id: String,
    val name: String,
)

data class FriendRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val avatarUrl: String? = null,
)

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val description: String? = null,
    val hasPremium: Boolean = false,
)


@Composable
fun FriendsScreenRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit,
    onFriendClick: (User) -> Unit,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val addFriend: () -> Unit =
        { viewModel.onEvent(FriendsScreenEvent.ShowAddFriendDialogEvent(true)) }
    val onEvent = viewModel::onEvent
    val errorState = uiState.errorState
    val errorTextId = uiState.errorText

    FriendsScreen(
        onBackClick,
        onFriendClick,
        uiState,
        addFriend,
        onEvent,
        errorState,
        errorTextId,
        sharedTransitionScope,
        animatedVisibilityScope
    )

}

@Composable
fun FriendsScreen(
    onBackClick: () -> Unit,
    onFriendClick: (User) -> Unit,
    uiState: FriendsScreenState,
    addFriend: () -> Unit,
    onEvent: (FriendsScreenEvent) -> Unit,
    errorState: Boolean = false,
    errorTextId: Int?,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val bg = ConstColours.BLACK
    val textColor = ConstColours.WHITE


    val userFriends = uiState.friends
    val isLoading = uiState.isLoading
    val showAddFriendDialog = uiState.showAddFriendDialog
    val showDeleteFriendDialog = uiState.showDeleteFriendDialog
    val addFriendQuery = uiState.addFriendQuery
    val searchQuery = uiState.searchQuery
    val selectedIndex = uiState.selectedIndex


    val filteredFriends = remember(userFriends, searchQuery) {
        if (searchQuery.isEmpty()) {
            userFriends
        } else {
            userFriends.filter { friend ->
                friend.name.contains(searchQuery, ignoreCase = true) ||
                        (friend.description?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(
                vertical = if (isPortrait) 14.dp else 8.dp,
                horizontal = if (isPortrait) 0.dp else 8.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (isPortrait) 14.dp else 24.dp,
                    vertical = if (isPortrait) 10.dp else 16.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BackCircleButton(
                    onClick = onBackClick
                )
                Spacer(modifier = Modifier.weight(1f))
                AddFriendCircleButton(
                    onClick = { addFriend() }
                )

            }

            Text(
                text = stringResource(R.string.friends_screen_headliner),
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(if (isPortrait) 40.dp else 24.dp))

        FriendSearchField(
            query = searchQuery,
            onQueryChange = { onEvent(FriendsScreenEvent.SearchQueryChange(it)) },
            modifier = Modifier.padding(horizontal = 16.dp),
            onSearch = { /* Можно добавить логику поиска */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            onRefresh = { onEvent(FriendsScreenEvent.RefreshPageEvent) },
            isRefreshing = uiState.isRefreshing
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color = ConstColours.MAIN_BRAND_BLUE,
                                strokeWidth = 3.dp
                            )
                            Text(
                                text = stringResource(R.string.friends_screen_search_loading),
                                color = textColor,
                                style = AppTextStyles.MainText
                            )
                        }
                    }
                } else {

                    if (uiState.friendRequests.isNotEmpty()) {


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = if (isPortrait) 28.dp else 32.dp,
                                    vertical = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.friend_requests),
                                color = textColor,
                                style = AppTextStyles.Headlines,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.friendRequests.size.toString(),
                                color = ConstColours.MAIN_BRAND_BLUE,
                                style = AppTextStyles.Headlines,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        FriendRequestCarousel(
                            uiState.friendRequests,
                            onEvent
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = if (isPortrait) 28.dp else 32.dp,
                                vertical = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.friends_screen_sub_headliner),
                            color = textColor,
                            style = AppTextStyles.Headlines,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = userFriends.size.toString(),
                            color = ConstColours.MAIN_BRAND_BLUE,
                            style = AppTextStyles.Headlines,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (filteredFriends.isEmpty()) {
                        if (searchQuery.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "😕",
                                    fontSize = 48.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = stringResource(R.string.friends_screen_search_nothing),
                                    color = ConstColours.SUPPORTING_TEXT,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = stringResource(R.string.friends_screen_search_support_mess),
                                    color = ConstColours.SUPPORTING_SUB_TEXT,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "👥",
                                    fontSize = 48.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = stringResource(R.string.friends_screen_no_friends),
                                    color = ConstColours.SUPPORTING_TEXT,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = stringResource(R.string.friends_screen_no_friends_support_mess),
                                    color = ConstColours.SUPPORTING_SUB_TEXT,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(
                                    horizontal = if (isPortrait) 16.dp else 24.dp,
                                    vertical = 8.dp
                                ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = filteredFriends,
                                key = { it.id }
                            ) { friend ->
                                FriendItem(
                                    modifier = Modifier.animateItem(),
                                    friend = friend,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    onFriendClick = onFriendClick,
                                    onItemSwipe = {
                                        onEvent(
                                            FriendsScreenEvent.ShowDeleteFriendDialogEvent(
                                                true,
                                                friend
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    }
    if (showDeleteFriendDialog) {
        Dialog(
            onDismissRequest = {
                onEvent(
                    FriendsScreenEvent.ShowDeleteFriendDialogEvent(
                        false,
                        uiState.friendToDelete
                    )
                )
            }
        ) {
            DeleteFriendDialog(
                friendToDelete = uiState.friendToDelete,
                onAccept = {
                    onEvent(
                        FriendsScreenEvent.DeleteFriendEvent
                    )
                    onEvent(
                        FriendsScreenEvent.ShowDeleteFriendDialogEvent(
                            false,
                            uiState.friendToDelete
                        )
                    )
                },
                onDismiss = {
                    onEvent(
                        FriendsScreenEvent.ShowDeleteFriendDialogEvent(
                            false,
                            uiState.friendToDelete
                        )
                    )
                }
            )
        }
    }

    if (showAddFriendDialog) {
        Dialog(
            onDismissRequest = { onEvent(FriendsScreenEvent.ShowAddFriendDialogEvent(false)) }
        ) {
            AddFriendDialog(
                value = addFriendQuery,
                selectedIndex = selectedIndex,
                onEvent = onEvent,
                onValueChange = { onEvent(FriendsScreenEvent.AddFriendQueryChange(it)) },
                isError = errorState,
                errorText = if (errorTextId != null && errorState) stringResource(errorTextId) else ""
            )
        }
    }
}


@Composable
fun FriendButton(
    modifier: Modifier = Modifier,
    friend: User,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {

    val avatarModifier = if (
        sharedTransitionScope != null && animatedVisibilityScope != null
    ) {
        with(sharedTransitionScope) {
            modifier.sharedElement(
                sharedContentState = rememberSharedContentState(
                    key = "person-avatar-${friend.id}"
                ),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ ->
                    tween(750)
                }
            )
        }
    } else {
        modifier
    }

    Box(
        modifier = avatarModifier
            .aspectRatio(1.0f)
            .clip(CircleShape)
            .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
    ) {
        if (friend.avatarUrl.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = stringResource(R.string.account_avatar),
                tint = ConstColours.ACCOUNT_LOGO_TINT,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        } else {
            AsyncImage(
                model = friend.avatarUrl,
                contentDescription = stringResource(R.string.account_avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(CircleShape)
            )
        }

    }
}


@Composable
fun FriendItem(
    modifier: Modifier = Modifier,
    friend: User,
    onFriendClick: (User) -> Unit,
    onFriendLongTap: () -> Unit = {},
    onItemSwipe: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {

    val swipeState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()



    SwipeToDismissBox(
        state = swipeState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = ConstColours.RED,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = ConstColours.WHITE
                )
            }
        },
        enableDismissFromStartToEnd = false,
        onDismiss = { it ->
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onItemSwipe()
                scope.launch {
                    swipeState.reset()
                }
            }
        }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(ConstColours.MAIN_BACK_GRAY)
                .combinedClickable(
                    onClick = { onFriendClick(friend) },
                    onLongClick = { onFriendLongTap() }
                )
        ) {
            Box(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                FriendButton(
                    friend = friend,
                    modifier = Modifier
                        .width(67.dp)
                        .height(67.dp)
                        .padding(3.dp),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )

                if (friend.isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(ConstColours.MAIN_BRAND_BLUE)
                            .border(
                                width = 2.dp,
                                color = ConstColours.BLACK,
                                shape = CircleShape
                            )
                    )
                }
            }
            if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                with(sharedTransitionScope) {
                    if (friend.description != null) {

                        Column {
                            Text(
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(
                                        key = "person-name-${friend.id}"
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                                    boundsTransform = { _, _ ->
                                        tween(750)
                                    }
                                ),
                                text = friend.name,
                                color = ConstColours.WHITE,
                                style = AppTextStyles.MainText
                            )
                            if (friend.description.isNotEmpty()) {
                                Text(
                                    friend.description,
                                    color = ConstColours.WHITE,
                                    style = AppTextStyles.SupportingText
                                )
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier.sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = "person-name-${friend.id}"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                                boundsTransform = { _, _ ->
                                    tween(750)
                                }
                            ),
                            text = friend.name,
                            color = ConstColours.WHITE,
                            style = AppTextStyles.MainText
                        )
                    }
                }
            } else {
                if (friend.description != null) {
                    Column {
                        Text(
                            modifier = Modifier,
                            text = friend.name,
                            color = ConstColours.WHITE,
                            style = AppTextStyles.MainText
                        )
                        if (friend.description.isNotEmpty()) {
                            Text(
                                friend.description,
                                color = ConstColours.WHITE,
                                style = AppTextStyles.SupportingText
                            )
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier,
                        text = friend.name,
                        color = ConstColours.WHITE,
                        style = AppTextStyles.MainText
                    )
                }
            }

        }

    }


}

@Preview(
    name = "Friend Item",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun FriendsScreenPreview() {
    FriendsScreen(
        {},
        {},
        FriendsScreenState(
            friends = listOf(User("123", "User 1", "email1"), User("321", "User 2", "email2")),
            friendRequests = listOf(
                FriendRequest("312", "132", "User 3"),
                FriendRequest("313", "134", "User 4"),
                FriendRequest("22", "2322", "User 5")
            )
        ),
        {}, {},
        errorState = false, errorTextId = null
    )
}


@Preview(
    name = "Friend Item",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun FriendItemPreview() {
    MomentumTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ConstColours.BLACK)
                .padding(16.dp)
        ) {
            FriendItem(
                friend = User(
                    id = "preview1",
                    name = "Тестовый Друг",
                    email = "test.email",
                    avatarUrl = null,
                    isOnline = true,
                ),
                onFriendClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            FriendItem(
                friend = User(
                    id = "preview2",
                    name = "Друг со статусом",
                    email = "test.email.2",
                    avatarUrl = null,
                    description = "С описанием"
                ),
                onFriendClick = {}
            )
        }
    }
}
