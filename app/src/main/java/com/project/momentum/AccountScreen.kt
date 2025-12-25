@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import androidx.compose.ui.unit.sp
import com.example.momentum.ConstColours
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.json.Json


class AccountViewModel : BasePostViewModel() {
    override fun addPhoto(context: Context, url: String) {
        val event = readRandomEvent(context)
        val postData = PostData(
            url = url,
            name = "userName", // В аккаунте всегда userName
            date = event.date,
            description = event.description
        )
        _posts.add(0, postData)
    }
}


@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    userName: String = stringResource(R.string.userName),
    userStatus: String = stringResource(R.string.account_online_status),
    postsCount: Int = 0,
    onPostClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: AccountViewModel
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = Color(0xFFEDEEF2)
    val textColor = Color.White
    val context: Context = LocalContext.current

    //var photos by remember { mutableStateOf<List<String>>(emptyList()) }
    //val photos = viewModel.photos
    val posts = viewModel.posts

    // Функция для добавления новой фотографии
    fun addNewPhoto() {
        val newPhotoUrl = "https://picsum.photos/300/300?random=${System.currentTimeMillis()}"
        // Добавляем новое фото в начало
        //photos = listOf(newPhotoUrl) + photos
        viewModel.addPhoto(context, newPhotoUrl)
    }

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
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackCircleButton(
                onClick = onBackClick
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
                    contentDescription = stringResource(R.string.account_avatar),
                    tint = iconTint.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
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
                text = stringResource(R.string.account_my_publications),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )

            PhotoGrid(
                posts = viewModel.posts,
                onPostClick = { post ->
                    viewModel.selectPost(post)
                    onPostClick()
                },
                onAddPhotoClick = { viewModel.addRandomPhoto(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showPlusButton = true,
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
            onProfileClick = {},
            onBackClick = {},
            viewModel = viewModel()
        )
    }
}