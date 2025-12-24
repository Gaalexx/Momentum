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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.momentum.Friend
import com.project.momentum.User
import com.project.momentum.UserViewModel



// Предположим, у вас есть модель данных для друга
data class Friend(
    val id: String,
    val name: String,
)

// Модель пользователя с друзьями
data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isOnline: Boolean = false,
    val description: String? = null,
    val friends: List<Friend>
)

// В вашем ViewModel или Composable функции
@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier,
    user: User,
    onBackClick: () -> Unit,
    viewModel: UserViewModel = viewModel()
) {
    val bg = ConstColours.BLACK
    val textColor = Color.White

    // Загружаем друзей для текущего пользователя при первом показе
    LaunchedEffect(user.id) {
        viewModel.loadFriendsForUser(user)
    }

    // Наблюдаем за состоянием из ViewModel
    val userFriends by viewModel.userFriends
    val isLoading by viewModel.isLoading

    var searchQuery by remember { mutableStateOf("") }

    // Для свайпа вниз
    var dragOffset by remember { mutableStateOf(0f) }
    val swipeThreshold = 50f

    // Фильтрация друзей по поисковому запросу
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

    // Определяем ориентацию экрана
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        // Свайп вниз (положительный Y)
                        val verticalDrag = dragAmount.y
                        if (verticalDrag > 50) { // Начинаем отслеживать только при значительном движении вниз
                            dragOffset = verticalDrag
                        }
                    },
                    onDragEnd = {
                        if (dragOffset > swipeThreshold) {
                            onBackClick()
                        }
                        dragOffset = 0f
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = if (isPortrait) 14.dp else 8.dp,
                    horizontal = if (isPortrait) 0.dp else 8.dp
                )
        ) {
            // Хедер
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (isPortrait) 14.dp else 24.dp,
                        vertical = if (isPortrait) 10.dp else 16.dp
                    )
            ) {
                // Кнопка назад слева
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    BackCircleButton(
                        onClick = onBackClick
                    )
                }

                // Заголовок по центру
                Text(
                    text = "Друзья",
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(if (isPortrait) 40.dp else 24.dp))

            // Поле поиска
            FriendSearchField(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                onSearch = { /* Можно добавить логику поиска */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Показать индикатор загрузки
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
                            text = "Загрузка друзей...",
                            color = Color(0xFF888888),
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                // Заголовок с количеством друзей
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
                        text = "Мои друзья",
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = userFriends.size.toString(),
                        color = ConstColours.MAIN_BRAND_BLUE,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Список друзей или пустое состояние
                if (filteredFriends.isEmpty()) {
                    if (searchQuery.isNotEmpty()) {
                        // Нет результатов поиска
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
                                text = "Друзья не найдены",
                                color = Color(0xFF888888),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Попробуйте изменить запрос",
                                color = Color(0xFF666666),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    } else {
                        // Список друзей пуст
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
                                text = "У вас пока нет друзей",
                                color = Color(0xFF888888),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Добавьте друзей, чтобы они появились здесь",
                                color = Color(0xFF666666),
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

                        // Добавляем отступ в конце списка
                        item {
                            Spacer(modifier = Modifier.height(if (isPortrait) 24.dp else 32.dp))
                        }
                    }
                }
            }
        }
    }
}

// Функция для получения контекста (добавьте в начале файла)
@Composable
fun rememberApplicationContext(): Context {
    val context = LocalContext.current
    return remember(context) { context.applicationContext }
}


@Composable
fun FriendButton(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    //TODO: Картинку запихнуть вместо Box
    Box(
        modifier = modifier
            .background(Color.Gray, CircleShape)
            .clip(CircleShape)
    )
}

// Отдельный компонент для отображения друга
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