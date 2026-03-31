package com.project.momentum.features.friends.ui.assets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.momentum.features.friends.ui.FriendRequest
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestCarousel(
    users: List<FriendRequest>,
    onEvent: (FriendsScreenEvent) -> Unit
) {
    val items = remember { users }
    val carouselState = rememberCarouselState { items.count() }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, top = 16.dp, bottom = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(
            count = items.size,
            key = { items[it].id }
        ) { index ->
            val item = items[index]
            FriendRequestCard(
                item,
                onEvent
            )

        }
    }

//    HorizontalMultiBrowseCarousel(
//        state = carouselState,
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .padding(top = 16.dp, bottom = 16.dp),
//        preferredItemWidth = 250.dp,
//        itemSpacing = 8.dp,
//        contentPadding = PaddingValues(horizontal = 16.dp)
//    ) { i ->
//        val item = items[i]
//        FriendRequestCard(
//            item,
//            onEvent
//        )
//    }
}