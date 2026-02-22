package com.project.momentum.navigation

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.project.momentum.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerNavGraph(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current

    // Pager state для вертикальной навигации между камерой и галереей
    // 0: камера, 1: галерея
    val pagerState = rememberPagerState(
        initialPage = 0, // начинаем с камеры
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

    // ViewModel'и - ОДИН экземпляр для всего приложения
    val accountVm: AccountViewModel = viewModel()
    val galleryVM: GalleryViewModel = viewModel()
    val friendsVM: UserViewModel = viewModel()

    Box(modifier = modifier.fillMaxSize()) {
        // Основной контент с вертикальными свайпами
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            reverseLayout = false, // свайп вверх = следующая страница (галерея)
            userScrollEnabled = true,
            beyondViewportPageCount = 1
        ) { page ->
            when (page) {
                0 -> CameraPagerPage(
                    accountVm = accountVm,
                    friendsVM = friendsVM,
                    onOpenGallery = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1) // свайп на галерею
                        }
                    },
                    onOpenRecorder = {
                        // Открываем рекордер через навигацию
                    },
                    modifier = Modifier.fillMaxSize()
                )

                1 -> GalleryPagerPage(
                    galleryVM = galleryVM,
                    accountVm = accountVm,
                    friendsVM = friendsVM,
                    onOpenCamera = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0) // свайп на камеру
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CameraPagerPage(
    accountVm: AccountViewModel,
    friendsVM: UserViewModel,
    onOpenGallery: () -> Unit,
    onOpenRecorder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Camera,
        modifier = modifier
    ) {
        composable<NavRoutes.Camera> {
            CameraLikeScreen(
                onGoToPreview = { uri ->
                    navController.navigate(NavRoutes.SendPhoto(uri.toString()))
                },
                onGoToRecorder = {
                    // Навигация на рекордер
                    navController.navigate(NavRoutes.Recorder)
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.Account(NavRoutes.Camera::class.qualifiedName!!))
                },
                onOpenGallery = onOpenGallery,
                onGoToSettings = {
                    navController.navigate(NavRoutes.Settings(NavRoutes.Camera::class.qualifiedName!!))
                },
                onGoToFriends = {
                    navController.navigate(NavRoutes.Friends)
                }
            )
        }

        composable<NavRoutes.Recorder> {
            RecorderScreen(
                navController = navController,
                onCameraClick = {
                    navController.popBackStack() // возврат на камеру
                },
                onGoToFriends = {
                    navController.navigate(NavRoutes.Friends)
                }
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
                    navController.popBackStack<NavRoutes.Camera>(false)
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

@Composable
fun GalleryPagerPage(
    galleryVM: GalleryViewModel,
    accountVm: AccountViewModel,
    friendsVM: UserViewModel,
    onOpenCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Gallery,
        modifier = modifier
    ) {
        composable<NavRoutes.Gallery> {
            GallaryScreen(
                onPostClick = { url ->
                    galleryVM.selectedPost?.let { post ->
                        navController.navigate(NavRoutes.PreviewPhoto(post.url))
                    }
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.Account(NavRoutes.Gallery::class.qualifiedName!!))
                },
                onBackClick = {
                    // Возврат на камеру через свайп
                    onOpenCamera()
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
                    navController.popBackStack<NavRoutes.Gallery>(false)
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
    }
}

// Удобная extension-функция для popBackStack
inline fun <reified T : Any> NavHostController.popBackStack(inclusive: Boolean = false) {
    popBackStack(route = T::class, inclusive = inclusive)
}