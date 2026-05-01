package com.project.momentum.features.cameracontentpager.ui

import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import com.project.momentum.features.contentcreation.ui.DefaultMaxRecordMs
import com.project.momentum.features.contentcreation.ui.MediaCreationScreen
import com.project.momentum.features.contentcreation.ui.assets.CameraTopBar
import com.project.momentum.features.posts.ui.WatchPhotoScreenRouteForMain
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.ui.theme.ConstColours


enum class MainScreenPage(val curPage: Int) {
    CONTENT_CREATION(0),
    MEDIA_VIEW(1)
}


@Composable
fun CameraContentPager(
    mainScreenPage: MainScreenPage = MainScreenPage.CONTENT_CREATION,
    initialMode: ContentCreationMode = ContentCreationMode.Camera,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onProfileClick: () -> Unit,
    onGoToGallery: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    maxRecordMs: Int = DefaultMaxRecordMs,
    onGoToTakePhoto: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    currentPost: Int = 0,
    postsViewModel: PostsViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(
        initialPage = mainScreenPage.ordinal,
        pageCount = { 2 }
    )

    val postsState = postsViewModel.state.collectAsStateWithLifecycle()
    Surface(
        color = ConstColours.BLACK
    ) {
        Column {
            CameraTopBar(
                onProfileClick = onProfileClick,
                onGoToSettings = onGoToSettings,
                onGoToFriends = onGoToFriends,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 14.dp),
            )

            Spacer(Modifier.height(5.dp))
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> MediaCreationScreen(
                        initialMode = initialMode,
                        onGoToPreview = onGoToPreview,
                        onProfileClick = onProfileClick,
                        onGoToSettings = onGoToSettings,
                        onGoToFriends = onGoToFriends,
                        maxRecordMs = maxRecordMs
                    )

                    1 -> if (postsState.value.posts.isNotEmpty()) {
                        WatchPhotoScreenRouteForMain(
                            onGoToTakePhoto = {},
                            onGoToGallery = onGoToGallery,
                            onProfileClick = onProfileClick,
                            onGoToSettings = onGoToSettings,
                            onGoToFriends = onGoToFriends,
                            postIndex = currentPost,
                            postsState.value.posts[currentPost].userId,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    } else {
                        Text("No posts yes") // TODO: сделать экран с No posts yes
                    }
                }
            }
        }
    }


}