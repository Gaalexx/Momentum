package com.project.momentum.features.cameracontentpager.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import com.project.momentum.features.contentcreation.ui.MediaCreationRoot
import com.project.momentum.features.newcontentcreation.ui.MyMediaCreationRoot
import com.project.momentum.features.newcontentcreation.ui.assets.CameraTopBar
import com.project.momentum.features.posts.ui.NoPostsYet
import com.project.momentum.features.posts.ui.WatchPhotoScreenRouteForMain
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.ui.theme.ConstColours
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
    //maxRecordMs: Int = DefaultMaxRecordMs,
    onGoToTakePhoto: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    currentPost: Int = 0,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()

    var enterAnimationFinished by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        enterAnimationFinished = false
        delay(500)
        enterAnimationFinished = true
    }

    val pagerState = rememberPagerState(
        initialPage = mainScreenPage.ordinal,
        pageCount = { 2 }
    )

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        snapPositionalThreshold = 0.125f
    )

    val isCameraPageActive by remember {
        derivedStateOf {
            enterAnimationFinished && pagerState.settledPage == MainScreenPage.CONTENT_CREATION.curPage
        }
    }

    BackHandler(enabled = pagerState.currentPage != 0) {
        scope.launch {
            pagerState.animateScrollToPage(0)
        }
    }


    val postsState = postsViewModel.state.collectAsStateWithLifecycle()
    Surface(
        color = ConstColours.BLACK,
        modifier = Modifier
//            .fillMaxSize()
            .background(ConstColours.BLACK)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column {
            CameraTopBar(
                onProfileClick = onProfileClick,
                onGoToSettings = onGoToSettings,
                onGoToFriends = onGoToFriends,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 14.dp),
            )

            Spacer(Modifier.height(5.dp))
            VerticalPager(
                state = pagerState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
//                    0 -> MediaCreationRoot(
//                        initialMode = initialMode,
//                        onGoToPreview = onGoToPreview,
//                        onProfileClick = onProfileClick,
//                        onGoToSettings = onGoToSettings,
//                        onGoToFriends = onGoToFriends,
//                        cameraPreviewEnabled = isCameraPageActive
//                    )
                    0 -> MyMediaCreationRoot(
                        onGoToPreview = { it -> },
                        onProfileClick = onProfileClick,
                        onGoToSettings = onGoToSettings,
                        onGoToFriends = onGoToFriends
                    )

                    1 -> if (postsState.value.posts.isNotEmpty()) {
                        WatchPhotoScreenRouteForMain(
                            onGoToTakePhoto = {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            },
                            onGoToGallery = onGoToGallery,
                            postIndex = currentPost,
                            userId = postsState.value.posts[currentPost].userId,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    } else {
                        NoPostsYet()
                    }
                }
            }
        }
    }


}
