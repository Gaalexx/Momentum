package com.project.momentum.navigation

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.project.momentum.*

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val accountVm: AccountViewModel = viewModel()
    val galleryVM: GalleryViewModel = viewModel()
    val friendsVM: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Camera,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {
        // Camera Screen - без аргументов
        composable<NavRoutes.Camera> {
            CameraLikeScreen(
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
                    navController.navigate(NavRoutes.Gallery) {
                        launchSingleTop = true
                        restoreState = true
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

        // Recorder Screen
        composable<NavRoutes.Recorder> {
            RecorderScreen(
                navController = navController,
                onCameraClick = {
                    navController.popBackStack()
                },
                onGoToFriends = {
                    navController.navigate(NavRoutes.Friends)
                }
            )
        }

        // Gallery Screen
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
                    navController.popBackStack()
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

        // Friends Screen
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

        // Account Screen - с аргументом
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

        // Settings Screen
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

        // Preview Photo Screen
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

        // Send Photo Screen
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

// Удобная extension-функция для popBackStack с типом
inline fun <reified T : Any> NavHostController.popBackStack(inclusive: Boolean = false) {
    popBackStack(T::class, inclusive)
}