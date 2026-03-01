package com.project.momentum.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.momentum.ui.screens.account.AccountScreen
import com.project.momentum.ui.screens.account.AccountViewModel
import com.project.momentum.ui.screens.camera.CameraLikeScreen
import com.project.momentum.ui.screens.camera.SendPhotoScreen
import com.project.momentum.ui.screens.friends.FriendsScreen
import com.project.momentum.ui.screens.friends.User
import com.project.momentum.ui.screens.friends.UserViewModel
import com.project.momentum.ui.screens.posts.GallaryScreen
import com.project.momentum.ui.screens.posts.GalleryViewModel
import com.project.momentum.ui.screens.posts.WatchPhotoScreen
import com.project.momentum.ui.screens.recorder.RecorderScreen
import com.project.momentum.ui.screens.settings.SettingsMainScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // ViewModel'и — один экземпляр на всё приложение
    val accountVm: AccountViewModel = viewModel()
    val galleryVM: GalleryViewModel = viewModel()
    val friendsVM: UserViewModel = viewModel()

    // Состояние для отслеживания, показываем ли мы поверхностный экран
    var isOverlayScreenVisible by remember { mutableStateOf(false) }

    // Состояние для текущего маршрута
    var currentRoute by remember { mutableStateOf<NavRoutes?>(null) }

    // Состояние для отслеживания режима камеры/рекордера
    var isRecorderMode by remember { mutableStateOf(false) }

    // Состояние для текущей страницы пейджера
    var currentPagerPage by remember { mutableStateOf(0) }

    Box(Modifier.fillMaxSize()) {

        // Pager всегда один, не пересоздаётся
        CameraGalleryPager(
            accountVm = accountVm,
            galleryVM = galleryVM,
            friendsVM = friendsVM,
            isRecorderMode = isRecorderMode,
            onRecorderModeChange = { isRecorderMode = it },
            onPageChange = { currentPagerPage = it },
            onNavigate = { route ->
                currentRoute = route
                isOverlayScreenVisible = true
            }
        )

        // Навигация поверх Pager с прозрачным фоном
        AnimatedContent(
            targetState = isOverlayScreenVisible,
            transitionSpec = {
                if (targetState) {
                    // Появление
                    fadeIn(animationSpec = tween(200)) with fadeOut(animationSpec = tween(100))
                } else {
                    // Исчезновение
                    fadeIn(animationSpec = tween(100)) with fadeOut(animationSpec = tween(200))
                }
            },
            label = "overlay_navigation"
        ) { visible ->
            if (visible && currentRoute != null) {
                // Показываем экран напрямую, без NavHost
                when (val route = currentRoute) {
                    is NavRoutes.Settings -> SettingsMainScreen(
                        onBackClick = {
                            scope.launch {
                                isOverlayScreenVisible = false
                                currentRoute = null
                            }
                        },
                        onPrivacyClick = {},
                        onNotificationsClick = {},
                        onDataClick = {},
                        onLanguageClick = {},
                        onPremiumClick = {},
                        onLogoutClick = {
                            scope.launch {
                                isOverlayScreenVisible = false
                                currentRoute = null
                            }
                        },
                        onDeleteAccountClick = {}
                    )

                    is NavRoutes.Account -> AccountScreen(
                        onPostClick = {
                            accountVm.selectedPost?.let { post ->
                                currentRoute = NavRoutes.PreviewPhoto(post.url)
                            }
                        },
                        onProfileClick = {},
                        onBackClick = {
                            scope.launch {
                                isOverlayScreenVisible = false
                                currentRoute = null
                            }
                        },
                        viewModel = accountVm
                    )

                    is NavRoutes.Friends -> {
                        val users by friendsVM.users
                        FriendsScreen(
                            user = users.firstOrNull() ?: User(
                                id = "default",
                                name = "Default User",
                                avatarUrl = "",
                                friends = emptyList()
                            ),
                            onBackClick = {
                                scope.launch {
                                    isOverlayScreenVisible = false
                                    currentRoute = null
                                }
                            },
                            viewModel = friendsVM
                        )
                    }

                    is NavRoutes.PreviewPhoto -> {
                        val post = accountVm.posts.find { it.url == route.url }
                            ?: galleryVM.posts.find { it.url == route.url }

                        if (post != null) {
                            WatchPhotoScreen(
                                onGoToTakePhoto = {
                                    scope.launch {
                                        isOverlayScreenVisible = false
                                        currentRoute = null
                                    }
                                },
                                onGoToGallery = {
                                    scope.launch {
                                        isOverlayScreenVisible = false
                                        currentRoute = null
                                    }
                                },
                                onGoToSettings = {
                                    // Передаем информацию о том, откуда пришли
                                    val backTo = when {
                                        currentPagerPage == 1 -> "gallery"
                                        isRecorderMode -> "recorder"
                                        else -> "camera"
                                    }
                                    currentRoute = NavRoutes.Settings(backTo)
                                },
                                onProfileClick = {
                                    // Передаем информацию о том, откуда пришли
                                    val backTo = when {
                                        currentPagerPage == 1 -> "gallery"
                                        isRecorderMode -> "recorder"
                                        else -> "camera"
                                    }
                                    currentRoute = NavRoutes.Account(backTo)
                                },
                                onGoToFriends = {
                                    currentRoute = NavRoutes.Friends
                                },
                                url = post.url,
                                description = post.description,
                                userName = post.name,
                                date = post.date
                            )
                        }
                    }

                    is NavRoutes.SendPhoto -> {
                        val uri = Uri.parse(route.uri)
                        SendPhotoScreen(
                            uri = uri,
                            onGoToTakePhoto = {
                                scope.launch {
                                    isOverlayScreenVisible = false
                                    currentRoute = null
                                }
                            },
                            onGoToSettings = {
                                // Передаем информацию о том, откуда пришли
                                val backTo = when {
                                    currentPagerPage == 1 -> "gallery"
                                    isRecorderMode -> "recorder"
                                    else -> "camera"
                                }
                                currentRoute = NavRoutes.Settings(backTo)
                            },
                            onProfileClick = {
                                // Передаем информацию о том, откуда пришли
                                val backTo = when {
                                    currentPagerPage == 1 -> "gallery"
                                    isRecorderMode -> "recorder"
                                    else -> "camera"
                                }
                                currentRoute = NavRoutes.Account(backTo)
                            },
                            onGoToFriends = {
                                currentRoute = NavRoutes.Friends
                            }
                        )
                    }

                    else -> { } // Camera, Recorder, Gallery не показываем в оверлее
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CameraGalleryPager(
    accountVm: AccountViewModel,
    galleryVM: GalleryViewModel,
    friendsVM: UserViewModel,
    isRecorderMode: Boolean,
    onRecorderModeChange: (Boolean) -> Unit,
    onPageChange: (Int) -> Unit,
    onNavigate: (NavRoutes) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    // Отслеживаем изменение страницы
    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pagerState.currentPage)
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> {
                // Страница 0 может показывать либо камеру, либо рекордер
                if (isRecorderMode) {
                    RecorderScreen(
                        onCameraClick = {
                            onRecorderModeChange(false) // Переключаем на камеру
                        },
                        onGoToFriends = {
                            onNavigate(NavRoutes.Friends)
                        },
                        onProfileClick = {
                            onNavigate(NavRoutes.Account("recorder"))
                        },
                        onGoToSettings = {
                            onNavigate(NavRoutes.Settings("recorder"))
                        }
                    )
                } else {
                    CameraLikeScreen(
                        onGoToPreview = { uri ->
                            onNavigate(NavRoutes.SendPhoto(uri.toString()))
                        },
                        onGoToRecorder = {
                            onRecorderModeChange(true) // Переключаем на рекордер
                        },
                        onProfileClick = {
                            onNavigate(NavRoutes.Account("camera"))
                        },
                        onOpenGallery = {
                            scope.launch { pagerState.animateScrollToPage(1) }
                        },
                        onGoToSettings = {
                            onNavigate(NavRoutes.Settings("camera"))
                        },
                        onGoToFriends = {
                            onNavigate(NavRoutes.Friends)
                        }
                    )
                }
            }

            1 -> GallaryScreen(
                onPostClick = {
                    galleryVM.selectedPost?.let { post ->
                        onNavigate(NavRoutes.PreviewPhoto(post.url))
                    }
                },
                onProfileClick = {
                    onNavigate(NavRoutes.Account("gallery"))
                },
                onBackClick = {
                    scope.launch { pagerState.animateScrollToPage(0) }
                },
                onGoToSettings = {
                    onNavigate(NavRoutes.Settings("gallery"))
                },
                onGoToFriends = {
                    onNavigate(NavRoutes.Friends)
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