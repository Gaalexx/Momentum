package com.project.momentum.features.friends.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent
import com.project.momentum.features.friends.viewmodel.SelectedIndex
import com.project.momentum.ui.assets.ContinueButtonAdaptive
import com.project.momentum.ui.assets.SingleChoiceSegmentedButton
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.features.friends.ui.User
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.ui.theme.MomentumTheme
import kotlinx.coroutines.launch

@Composable
fun AddFriendsWithVKScreen(
    value: String,
    selectedIndex: SelectedIndex,
    onEvent: (FriendsScreenEvent) -> Unit = {},
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String? = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val vKFriends = listOf<User>(
        User(
            id = "preview1",
            name = "Тестовый Друг",
            email = "test.email",
            avatarUrl = null,
            isOnline = true,
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(ConstColours.BLACK)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(
                vertical = 14.dp,
                horizontal = 0.dp
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.4f)
                .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.friend_search),
                        color = ConstColours.WHITE,
                        style = AppTextStyles.Headlines
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextFieldRegistration(
                        value = value,
                        onValueChange = onValueChange,
                        isError = isError,
                        errorText = errorText,
                        placeholder = placeholder,
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        keyboardOptions = keyboardOptions, // TODO: изменять в зависимости от selectedIndex
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    SingleChoiceSegmentedButton(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        selectedIndex,
                        onEvent
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ContinueButtonAdaptive(
                        onClick = {
                            when (selectedIndex) {
                                SelectedIndex.EMAIL -> onEvent(
                                    FriendsScreenEvent.CreateFriendRequest.EmailRequest(
                                        value
                                    )
                                )

                                SelectedIndex.LOGIN -> onEvent(
                                    FriendsScreenEvent.CreateFriendRequest.LoginRequest(
                                        value
                                    )
                                )

                                else -> Unit
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        colors = ButtonColors(
                            containerColor = ConstColours.MAIN_BRAND_BLUE,
                            contentColor = ConstColours.WHITE,
                            disabledContentColor = ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                            disabledContainerColor = ConstColours.WHITE
                        ),
                        text = stringResource(R.string.send_request)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.friends_screen_add_vk_friends),
                color = ConstColours.WHITE,
                style = AppTextStyles.Headlines
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            if (vKFriends.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = vKFriends,
                        key = { it.id }
                    ) { friend ->
                        AddFriendItem(
                            modifier = Modifier.animateItem(),
                            friend = friend,
                            onAddFriendClick = {
                                onEvent(FriendsScreenEvent.CreateFriendRequest.LoginRequest(friend.id))
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AddFriendItem(
    modifier: Modifier = Modifier,
    friend: User,
    onAddFriendClick: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(ConstColours.MAIN_BACK_GRAY)
    ) {
        Box(modifier = Modifier.padding(end = 12.dp)) {

            Box(
                modifier = Modifier
                    .width(67.dp)
                    .height(67.dp)
                    .padding(3.dp)
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
        Text(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            text = friend.name,
            color = ConstColours.WHITE,
            style = AppTextStyles.MainText,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.weight(1f))

        ContinueButton(
            onClick = {onAddFriendClick()},
            modifier = Modifier
                .width(80.dp),
            colors = ButtonColors(
                containerColor = ConstColours.MAIN_BRAND_BLUE,
                contentColor = ConstColours.WHITE,
                disabledContentColor = ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                disabledContainerColor = ConstColours.WHITE
            ),
            text = stringResource(R.string.button_plus),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewPager() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AddFriendsWithVKScreen(
            "Что это",
            onEvent = {},
            selectedIndex = SelectedIndex.LOGIN,
            placeholder = "Введите имя",
            onValueChange = {},
            isError = true,
            errorText = "Ошибочка вышла"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AddFriendItemPreview() {
    MomentumTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ConstColours.BLACK)
                .padding(16.dp)
        ) {
            AddFriendItem(
                friend = User(
                    id = "preview1",
                    name = "Тестовый Друг",
                    email = "test.email",
                    avatarUrl = null,
                    isOnline = true,
                ),
                onAddFriendClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            AddFriendItem(
                friend = User(
                    id = "preview2",
                    name = "Друг со статусом",
                    email = "test.email.2",
                    avatarUrl = null,
                    description = "С описанием"
                ),
                onAddFriendClick = {}
            )
        }
    }
}