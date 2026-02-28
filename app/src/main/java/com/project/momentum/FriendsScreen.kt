package com.project.momentum

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.momentum.ConstColours
import com.project.momentum.ui.theme.AppTextStyles
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.delay
import android.content.res.Configuration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.momentum.Friend
import com.project.momentum.User
import com.project.momentum.UserViewModel



data class Friend(
    val id: String,
    val name: String,
)

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isOnline: Boolean = false,
    val description: String? = null,
    val friends: List<Friend>
)

@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier,
    user: User,
    onBackClick: () -> Unit,
    viewModel: UserViewModel = viewModel()
) {
    val bg = ConstColours.BLACK
    val textColor = ConstColours.WHITE

    LaunchedEffect(user.id) {
        viewModel.loadFriendsForUser(user)
    }

    val userFriends by viewModel.userFriends
    val isLoading by viewModel.isLoading

    var searchQuery by remember { mutableStateOf("") }

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
        modifier = modifier
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
            onQueryChange = { searchQuery = it },
            modifier = Modifier.padding(horizontal = 16.dp),
            onSearch = { /* Можно добавить логику поиска */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                            friend = friend
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FriendButton(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}


@Composable
fun FriendItem(friend: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 10.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(end = 12.dp)
        ) {
            FriendButton(
                imageUrl = friend.avatarUrl,
                modifier = Modifier
                    .width(67.dp)
                    .height(67.dp)
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

        if (friend.description != null) {
            Column {
                Text(
                    friend.name,
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
                friend.name,
                color = ConstColours.WHITE,
                style = AppTextStyles.MainText
            )
        }
    }
}


@Preview(
    name = "Friend Item",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun FriendItemPreview() {
    MaterialTheme {
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
                    avatarUrl = "",
                    isOnline = true,
                    friends = emptyList()
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            FriendItem(
                friend = User(
                    id = "preview2",
                    name = "Друг со статусом",
                    avatarUrl = "",
                    description = "С описанием",
                    friends = emptyList()
                )
            )
        }
    }
}