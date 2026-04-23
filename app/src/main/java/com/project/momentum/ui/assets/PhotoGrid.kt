package com.project.momentum.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.project.momentum.R
import com.project.momentum.features.account.models.PostData
import com.project.momentum.network.s3.MediaType
import com.project.momentum.ui.custom.gradientpicker.GradientPicker
import com.project.momentum.ui.custom.shapes.ScallopedShape
import com.project.momentum.ui.theme.ConstColours

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
                    is GridItem.Post -> item.post.id + item.post.presignedURL
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
                            model = post.presignedURL,
                            contentDescription = stringResource(
                                R.string.photo_grid_photo,
                                post.title
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
                            PlusButtonAdaptive(
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


@Composable
fun S3PhotoGrid(
    posts: List<PostData>,
    onPostClick: (Int) -> Unit = { },
    onAddPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
    showPlusButton: Boolean = true,
    columns: Int = 3
) {
    val displayItems = buildList<S3GridItem> {
        if (showPlusButton) add(S3GridItem.PlusButton)
        posts.forEach { post ->
            when (post.mediaType) {
                MediaType.IMAGE -> add(S3GridItem.Post(post))
                MediaType.AUDIO -> add(S3GridItem.AudioPost(post, GradientPicker.pick()))
                MediaType.VIDEO -> add(S3GridItem.VideoPost(post))
            }

        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(
            items = displayItems,
            key = { index, item ->
                when (item) {
                    is S3GridItem.Post -> "${item.post.id}_$index"
                    is S3GridItem.AudioPost -> "${item.post.id}_$index"
                    is S3GridItem.VideoPost -> "${item.post.id}_$index"
                    S3GridItem.PlusButton -> "PLUS"
                }
            }
        ) { index, item ->

            when (item) {
                is S3GridItem.Post -> {
                    val post = item.post
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ConstColours.MAIN_BACK_GRAY)
                    ) {
                        SubcomposeAsyncImage(
                            model = post.presignedURL,
                            contentDescription = stringResource(
                                R.string.photo_grid_photo,
                                post.userId
                            ),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    val postIndex = if (showPlusButton) index - 1 else index
                                    onPostClick(postIndex)
                                },
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
                }

                is S3GridItem.AudioPost -> {
                    val post = item.post
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clip(ScallopedShape())
                            .background(
                                brush = item.brush
                            )
                            .clickable {
                                val postIndex = if (showPlusButton) index - 1 else index
                                onPostClick(postIndex)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(0.5f),
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = stringResource(R.string.play),
                            tint = ConstColours.WHITE
                        )
                    }
                }

                is S3GridItem.VideoPost -> {
                    val post = item.post
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ConstColours.MAIN_BACK_GRAY)
                            .clickable {
                                val postIndex = if (showPlusButton) index - 1 else index
                                onPostClick(postIndex)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        VideoThumbnail(post.presignedURL)
                        Icon(
                            modifier = Modifier.fillMaxSize(0.5f),
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = stringResource(R.string.play),
                            tint = ConstColours.WHITE
                        )
                    }
                }

                S3GridItem.PlusButton -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ConstColours.MAIN_BACK_GRAY),
                        contentAlignment = Alignment.Center
                    ) {
                        PlusButtonAdaptive(
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


sealed class S3GridItem {
    data class Post(val post: PostData) : S3GridItem()
    data class VideoPost(val post: PostData) : S3GridItem()
    data class AudioPost(val post: PostData, val brush: Brush) : S3GridItem()
    data object PlusButton : S3GridItem()
}
