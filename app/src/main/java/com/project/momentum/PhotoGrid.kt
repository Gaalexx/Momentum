package com.project.momentum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.momentum.ConstColours


@Composable
fun PhotoGrid(
    photos: List<String>,
    onPostClick: (String) -> Unit = {},
    onAddPhotoClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    showPlusButton: Boolean = true,
    columns: Int = 3
) {
    // Собираем все элементы для отображения
    val displayItems = mutableListOf<GridItem>()

    // Если нужно показывать кнопку плюса, добавляем ее первой
    if (showPlusButton) {
        displayItems.add(GridItem.PlusButton)
    }

    // Добавляем все фотографии
    photos.forEachIndexed { index, url ->
        displayItems.add(GridItem.Photo(index, url))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(displayItems) { _, item ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ConstColours.MAIN_BACK_GRAY)
            ) {
                when (item) {
                    is GridItem.Photo -> {
                        SubcomposeAsyncImage(
                            model = item.url,
                            contentDescription = stringResource(R.string.photo_grid_photo, item.index),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onPostClick(item.url) },
                            loading = {
                                // Только индикатор загрузки
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = ConstColours.MAIN_BRAND_BLUE,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        )
                    }

                    GridItem.PlusButton -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            PlusButton(
                                onClick = onAddPhotoClick,
                                modifier = Modifier.size(48.dp),
                                backgroundColor = ConstColours.MAIN_BRAND_BLUE,
                                iconColor = ConstColours.WHITE
                            )
                        }
                    }
                }
            }
        }
    }
}

// Модель для элементов сетки
sealed class GridItem {
    data class Photo(val index: Int, val url: String) : GridItem()
    object PlusButton : GridItem()
}
