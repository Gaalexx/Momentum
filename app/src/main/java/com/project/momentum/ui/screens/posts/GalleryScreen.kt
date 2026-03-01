@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.ui.screens.posts

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.momentum.ConstColours
import com.project.momentum.PhotoGrid
import com.project.momentum.R
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton


class GalleryViewModel : BasePostViewModel() {
    override fun addPhoto(context: Context, url: String) {
        val event = readRandomEvent(context)
        val postData = PostData(
            url = url,
            name = event.name, // В галерее имя из JSON
            date = event.date,
            description = event.description
        )
        _posts.add(0, postData)
    }
}


@Composable
fun GallaryScreen(
    modifier: Modifier = Modifier,
    onPostClick: (String) -> Unit = {},
    onAddPhoto: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    viewModel: GalleryViewModel
) {
    val bg = ConstColours.BLACK
    val textColor = Color.White

    var dragOffset by remember { mutableStateOf(0f) }
    val swipeThreshold = 50f
    val context: Context = LocalContext.current

    //var photos by remember { mutableStateOf<List<String>>(emptyList()) }
    //val photos = viewModel.photos
    val posts = viewModel.posts

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(Unit) {
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileCircleButton(onClick = onProfileClick)

                Spacer(Modifier.weight(1f))

                FriendsPillButton(onClick = onGoToFriends)

                Spacer(Modifier.weight(1f))

                SettingsCircleButton(onClick = onGoToSettings)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.gallery_title),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PhotoGrid(
                posts = viewModel.posts,
                onPostClick =
                    { post ->
                        viewModel.selectPost(post)
                        onPostClick(post.url)
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
private fun GallaryScreenPreview() {
    MaterialTheme {
        GallaryScreen(
            onPostClick = {},
            onProfileClick = {},
            onBackClick = {},
            onGoToSettings = {},
            onGoToFriends = {},
            viewModel = viewModel()
        )
    }
}