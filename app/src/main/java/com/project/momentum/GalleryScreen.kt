@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.momentum.ConstColours


class GalleryViewModel : ViewModel() {

    val photos = mutableStateListOf<String>()

    fun addPhoto(url: String) {
        photos.add(0, url)
    }

    fun addRandomPhoto() {
        addPhoto("https://picsum.photos/300/300?random=${System.currentTimeMillis()}")
    }

    fun removePhoto(url: String) {
        photos.remove(url)
    }

    fun clear() {
        photos.clear()
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
    galleryVM: GalleryViewModel
) {
    val bg = ConstColours.BLACK
    val textColor = Color.White

    var dragOffset by remember { mutableStateOf(0f) }
    val swipeThreshold = 50f

    //var photos by remember { mutableStateOf<List<String>>(emptyList()) }
    val photos = galleryVM.photos


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        val verticalDrag = dragAmount.y
                        if (verticalDrag > 50) {
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

                FriendsPillButton(onClick = {})

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
                photos = photos,
                onPostClick = onPostClick,
                onAddPhotoClick = { galleryVM.addRandomPhoto() },
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
            galleryVM = GalleryViewModel()
        )
    }
}