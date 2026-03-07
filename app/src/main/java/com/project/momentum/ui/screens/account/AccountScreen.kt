@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.momentum.ConstColours
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.project.momentum.PhotoGrid
import com.project.momentum.R
import com.project.momentum.S3PhotoGrid
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.screens.posts.BasePostViewModel
import com.project.momentum.ui.screens.posts.PostData
import com.project.momentum.ui.viewmodel.AccountViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    userName: String = stringResource(R.string.userName),
    userStatus: String = stringResource(R.string.account_online_status),
    postsCount: Int = 0,
    onPostClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = Color(0xFFEDEEF2)
    val textColor = Color.White
    val context: Context = LocalContext.current

    val viewModel: AccountViewModel = hiltViewModel()

    val posts = viewModel.posts

    fun addNewPhoto() {
        viewModel.loadPosts()
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(chrome2)
                    .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
            ) {
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

            Text(
                text = userName,
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

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
                posts = viewModel.posts.collectAsState().value,
                onPostClick = {},
                onAddPhotoClick = { viewModel.loadPosts() },
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
            onBackClick = {}
        )
    }
}
