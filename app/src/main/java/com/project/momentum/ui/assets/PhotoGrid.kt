package com.project.momentum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.momentum.ConstColours
import com.project.momentum.ui.assets.PlusButton
import com.project.momentum.ui.screens.posts.PostData

@Composable
fun PhotoGrid(
    posts: List<PostData>,
    onPostClick: (PostData) -> Unit = {},
    onAddPhotoClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    showPlusButton: Boolean = true,
    columns: Int = 3
) {
    val displayItems = buildList<GridItem> {
        if (showPlusButton) add(GridItem.PlusButton)
        posts.forEach { post -> add(GridItem.Post(post)) }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(
            items = displayItems,
            key = { item ->
                when (item) {
                    is GridItem.Post -> item.post.url
                    GridItem.PlusButton -> "PLUS"
                }
            }
        ) { item ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ConstColours.MAIN_BACK_GRAY)
            ) {
                when (item) {
                    is GridItem.Post -> {
                        val post = item.post
                        SubcomposeAsyncImage(
                            model = post.url,
                            contentDescription = stringResource(
                                R.string.photo_grid_photo,
                                post.name
                            ),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onPostClick(post) },
                            loading = {
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

sealed class GridItem {
    data class Post(val post: PostData) : GridItem()
    data object PlusButton : GridItem()
}
