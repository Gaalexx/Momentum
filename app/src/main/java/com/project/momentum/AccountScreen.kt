@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.momentum.ConstColours

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    userName: String = "userName",
    userStatus: String = "В сети",
    postsCount: Int = 0,
    //onEditProfile: () -> Unit = {},
    onPostClick: (Int) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = Color(0xFFEDEEF2)
    val textColor = Color.White

    // Моковые данные для постов
    val mockPosts = List(18) { index ->
        "https://picsum.photos/300/300?random=$index"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.Start
    ) {
        // Верхняя панель

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackCircleButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Информация профиля
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Аватарка
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(chrome2)
                    .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
            ) {
                // Здесь можно добавить реальное изображение профиля
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Аватар",
                    tint = iconTint.copy(alpha = 0.7f),
                    modifier = Modifier.size(80.dp).align(Alignment.Center)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Имя пользователя
            Text(
                text = userName,
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            // Статус
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = userStatus,
                    color = Color(0xFFA0A0A0),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Раздел "Мои публикации"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(chrome2.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Мои публикации",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(mockPosts) { postUrl ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onPostClick(mockPosts.indexOf(postUrl))
                            }
                    ) {
                        AsyncImage(
                            model = postUrl,
                            contentDescription = "Пост",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun AccountScreenPreview() {
    MaterialTheme {
        AccountScreen(
            //onEditProfile = {},
            onPostClick = {},
            onProfileClick = {},
            onBackClick = {}
        )
    }
}