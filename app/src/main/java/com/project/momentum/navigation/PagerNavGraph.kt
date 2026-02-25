package com.project.momentum.navigation

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.project.momentum.AccountScreen
import com.project.momentum.AccountViewModel
import com.project.momentum.CameraLikeScreen
import com.project.momentum.FriendsScreen
import com.project.momentum.GalleryViewModel
import com.project.momentum.GallaryScreen
import com.project.momentum.RecorderScreen
import com.project.momentum.SendPhotoScreen
import com.project.momentum.SettingsMainScreen
import com.project.momentum.User
import com.project.momentum.UserViewModel
import com.project.momentum.WatchPhotoScreen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var lastCaptureRoute by remember { mutableStateOf<NavRoutes>(NavRoutes.Camera) }

    // ViewModel'и - ОДИН экземпляр для всего приложения
    val accountVm: AccountViewModel = viewModel()
    val galleryVM: GalleryViewModel = viewModel()
    val friendsVM: UserViewModel = viewModel()

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            when (entry.destination.route) {
                NavRoutes.Camera::class.qualifiedName -> lastCaptureRoute = NavRoutes.Camera
                NavRoutes.Recorder::class.qualifiedName -> lastCaptureRoute = NavRoutes.Recorder
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Camera,
        modifier = modifier
    ) {
        composable<NavRoutes.Camera> {
            CameraGalleryPager(
                navController = navController,
                accountVm = accountVm,
                galleryVM = galleryVM,
                friendsVM = friendsVM,
                initialPage = 0,
                page0Route = NavRoutes.Camera,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<NavRoutes.Recorder> {
            CameraGalleryPager(
                navController = navController,
                accountVm = accountVm,
                galleryVM = galleryVM,
                friendsVM = friendsVM,
                initialPage = 0,
                page0Route = NavRoutes.Recorder,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<NavRoutes.Gallery> {
            CameraGalleryPager(
                navController = navController,
                accountVm = accountVm,
                galleryVM = galleryVM,
                friendsVM = friendsVM,
                initialPage = 1,
                page0Route = lastCaptureRoute,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<NavRoutes.Settings> { backStackEntry ->
            val settingsRoute: NavRoutes.Settings = backStackEntry.toRoute()
            SettingsMainScreen(
                onBackClick = {
                    navController.popBackStack(settingsRoute.backTo, false)
                },
                onPrivacyClick = { },
                onNotificationsClick = { },
                onDataClick = { },
                onLanguageClick = { },
                onPremiumClick = { },
                onLogoutClick = {
                    val galleryRoute = NavRoutes.Gallery::class.qualifiedName
                    val backTo = settingsRoute.backTo
                    if (backTo != null && backTo == galleryRoute) {
                        navController.popBackStack<NavRoutes.Gallery>(false)
                    } else {
                        navController.popBackStack<NavRoutes.Camera>(false)
                    }
                },
                onDeleteAccountClick = { }
            )
        }

        composable<NavRoutes.Account> { backStackEntry ->
            val accountRoute: NavRoutes.Account = backStackEntry.toRoute()
            AccountScreen(
                onPostClick = {
                    accountVm.selectedPost?.let { post ->
                        navController.navigate(NavRoutes.PreviewPhoto(post.url))
                    }
                },
                onProfileClick = { },
                onBackClick = {
                    navController.popBackStack(accountRoute.backTo, false)
                },
                viewModel = accountVm
            )
        }

        composable<NavRoutes.Friends> {
            val users by friendsVM.users
            FriendsScreen(
                user = users.firstOrNull() ?: User(
                    id = "default",
                    name = "Default User",
                    avatarUrl = "",
                    friends = emptyList()
                ),
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = friendsVM
            )
        }

        composable<NavRoutes.PreviewPhoto> { backStackEntry ->
            val previewRoute: NavRoutes.PreviewPhoto = backStackEntry.toRoute()
            val post = accountVm.posts.find { it.url == previewRoute.url }
                ?: galleryVM.posts.find { it.url == previewRoute.url }

            if (post != null) {
                WatchPhotoScreen(
                    onGoToTakePhoto = {
                        navController.popBackStack<NavRoutes.Camera>(false)
                    },
                    onGoToGallery = {
                        navController.popBackStack<NavRoutes.Gallery>(false)
                    },
                    onGoToSettings = {
                        navController.navigate(NavRoutes.Settings(NavRoutes.PreviewPhoto::class.qualifiedName!!))
                    },
                    onProfileClick = {
                        navController.navigate(NavRoutes.Account(NavRoutes.PreviewPhoto::class.qualifiedName!!))
                    },
                    onGoToFriends = {
                        navController.navigate(NavRoutes.Friends)
                    },
                    url = post.url,
                    description = post.description,
                    userName = post.name,
                    date = post.date
                )
            }
        }

        composable<NavRoutes.SendPhoto> { backStackEntry ->
            val sendRoute: NavRoutes.SendPhoto = backStackEntry.toRoute()
            val uri = Uri.parse(sendRoute.uri)
            SendPhotoScreen(
                uri = uri,
                onGoToTakePhoto = {
                    navController.popBackStack()
                },
                onGoToSettings = {
                    navController.navigate(NavRoutes.Settings(NavRoutes.SendPhoto::class.qualifiedName!!))
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.Account(NavRoutes.SendPhoto::class.qualifiedName!!))
                },
                onGoToFriends = {
                    navController.navigate(NavRoutes.Friends)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CameraGalleryPager(
    navController: NavHostController,
    accountVm: AccountViewModel,
    galleryVM: GalleryViewModel,
    friendsVM: UserViewModel,
    initialPage: Int,
    page0Route: NavRoutes,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { 2 }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState, page0Route) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                val targetRoute = if (page == 0) page0Route else NavRoutes.Gallery
                val targetRouteName = if (page == 0) {
                    page0Route::class.qualifiedName
                } else {
                    NavRoutes.Gallery::class.qualifiedName
                }
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (targetRouteName != null && currentRoute != targetRouteName) {
                    navController.navigate(targetRoute) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            }
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        reverseLayout = false,
        userScrollEnabled = true,
        beyondViewportPageCount = 1
    ) { page ->
        when (page) {
            0 -> when (page0Route) {
                NavRoutes.Recorder -> RecorderScreen(
                    navController = navController,
                    onCameraClick = {
                        navController.navigate(NavRoutes.Camera)
                    },
                    onGoToFriends = {
                        navController.navigate(NavRoutes.Friends)
                    }
                )

                else -> CameraLikeScreen(
                    onGoToPreview = { uri ->
                        navController.navigate(NavRoutes.SendPhoto(uri.toString()))
                    },
                    onGoToRecorder = {
                        navController.navigate(NavRoutes.Recorder)
                    },
                    onProfileClick = {
                        navController.navigate(NavRoutes.Account(NavRoutes.Camera::class.qualifiedName!!))
                    },
                    onOpenGallery = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    onGoToSettings = {
                        navController.navigate(NavRoutes.Settings(NavRoutes.Camera::class.qualifiedName!!))
                    },
                    onGoToFriends = {
                        navController.navigate(NavRoutes.Friends)
                    }
                )
            }

            1 -> GallaryScreen(
                onPostClick = { _ ->
                    galleryVM.selectedPost?.let { post ->
                        navController.navigate(NavRoutes.PreviewPhoto(post.url))
                    }
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.Account(NavRoutes.Gallery::class.qualifiedName!!))
                },
                onBackClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                onGoToSettings = {
                    navController.navigate(NavRoutes.Settings(NavRoutes.Gallery::class.qualifiedName!!))
                },
                onGoToFriends = {
                    navController.navigate(NavRoutes.Friends)
                },
                viewModel = galleryVM
            )
        }
    }
}

// Удобная extension-функция для popBackStack
inline fun <reified T : Any> NavHostController.popBackStack(inclusive: Boolean = false) {
    popBackStack(route = T::class, inclusive = inclusive)
}
