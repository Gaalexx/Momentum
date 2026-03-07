package com.project.momentum.navigation

import android.net.Uri
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.project.momentum.ui.screens.account.AccountScreen
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

@Composable
fun MainScreen() {
    val galleryVM: GalleryViewModel = viewModel()
    val friendsVM: UserViewModel = viewModel()

    val backStack = rememberNavBackStack(NavRoutes.Camera)

    fun setBase(route: NavRoutes) {
        if (backStack.isEmpty()) {
            backStack.add(route)
            return
        }
        backStack[0] = route
    }

    fun openOverlay(route: NavRoutes) {
        val last = backStack.lastOrNull()
        if (last != null && last.isOverlayRoute()) {
            backStack[backStack.lastIndex] = route
            return
        }
        backStack.add(route)
    }

    fun closeOverlay() {
        val last = backStack.lastOrNull()
        if (last != null && last.isOverlayRoute() && backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    fun closeGallery() {
        val last = backStack.lastOrNull()
        if (last is NavRoutes.Gallery && backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        } else {
            setBase(NavRoutes.Camera)
        }
    }

    fun currentBackTo(): String {
        val lastBase = backStack.lastOrNull { !it.isOverlayRoute() }
        return when (lastBase) {
            is NavRoutes.Gallery -> "gallery"
            is NavRoutes.Recorder -> "recorder"
            else -> "camera"
        }
    }


    val noAnimation: () -> ContentTransform = {
        ContentTransform(EnterTransition.None, ExitTransition.None)
    }

    val saveableStateHolder = rememberSaveableStateHolder()
    val entryDecorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator<NavKey>(saveableStateHolder),
        rememberViewModelStoreNavEntryDecorator<NavKey>()
    )

    val navEntryProvider = entryProvider<NavKey> {
        entry<NavRoutes.Camera> {
            CameraLikeScreen(
                onGoToPreview = { uri ->
                    openOverlay(NavRoutes.SendPhoto(uri.toString()))
                },
                onGoToRecorder = {
                    setBase(NavRoutes.Recorder)
                },
                onProfileClick = {
                    openOverlay(NavRoutes.Account("camera"))
                },
                onGoToSettings = {
                    openOverlay(NavRoutes.Settings("camera"))
                },
                onGoToFriends = {
                    openOverlay(NavRoutes.Friends)
                },
                onOpenGallery = {}
            )
        }

        entry<NavRoutes.Recorder> {
            RecorderScreen(
                onCameraClick = {
                    setBase(NavRoutes.Camera)
                },
                onGoToFriends = {
                    openOverlay(NavRoutes.Friends)
                },
                onProfileClick = {
                    openOverlay(NavRoutes.Account("recorder"))
                },
                onGoToSettings = {
                    openOverlay(NavRoutes.Settings("recorder"))
                }
            )
        }

        entry<NavRoutes.Gallery> {
            GallaryScreen(
                onPostClick = {
                    galleryVM.selectedPost?.let { post ->
                        openOverlay(NavRoutes.PreviewPhoto(post.url))
                    }
                },
                onProfileClick = {
                    openOverlay(NavRoutes.Account("gallery"))
                },
                onBackClick = {
                    closeGallery()
                },
                onGoToSettings = {
                    openOverlay(NavRoutes.Settings("gallery"))
                },
                onGoToFriends = {
                    openOverlay(NavRoutes.Friends)
                },
                viewModel = galleryVM
            )
        }

        entry<NavRoutes.Settings> {
            SettingsMainScreen(
                onBackClick = {
                    closeOverlay()
                },
                onPremiumClick = {},
                onLogoutClick = {
                    closeOverlay()
                },
                onDeleteAccountClick = {}
            )
        }

        entry<NavRoutes.Account> {
            AccountScreen(
                onPostClick = {},
                onProfileClick = {},
                onBackClick = {
                    closeOverlay()
                }
            )
        }

        entry<NavRoutes.Friends> {
            val users by friendsVM.users
            FriendsScreen(
                user = users.firstOrNull() ?: User(
                    id = "default",
                    name = "Default User",
                    avatarUrl = "",
                    friends = emptyList()
                ),
                onBackClick = {
                    closeOverlay()
                },
                viewModel = friendsVM
            )
        }

        entry<NavRoutes.SendPhoto> { route ->
            val uri = Uri.parse(route.uri)
            SendPhotoScreen(
                uri = uri,
                onGoToTakePhoto = {
                    closeOverlay()
                },
                onGoToSettings = {
                    openOverlay(NavRoutes.Settings(currentBackTo()))
                },
                onProfileClick = {
                    openOverlay(NavRoutes.Account(currentBackTo()))
                },
                onGoToFriends = {
                    openOverlay(NavRoutes.Friends)
                }
            )
        }

        entry<NavRoutes.PreviewPhoto> { route ->
            val post = galleryVM.posts.find { it.url == route.url }
                ?: galleryVM.selectedPost

            if (post != null) {
                WatchPhotoScreen(
                    onGoToTakePhoto = {
                        closeOverlay()
                    },
                    onGoToGallery = {
                        closeOverlay()
                    },
                    onGoToSettings = {
                        openOverlay(NavRoutes.Settings(currentBackTo()))
                    },
                    onProfileClick = {
                        openOverlay(NavRoutes.Account(currentBackTo()))
                    },
                    onGoToFriends = {
                        openOverlay(NavRoutes.Friends)
                    },
                    url = post.url,
                    description = post.description,
                    userName = post.name,
                    date = post.date
                )
            }
        }
    }

    val entries = rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = entryDecorators,
        entryProvider = navEntryProvider
    )

    NavDisplay(
        entries = entries,
        transitionSpec = { noAnimation() },
        popTransitionSpec = { noAnimation() },
        predictivePopTransitionSpec = { _ -> noAnimation() },
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        }
    )
}

private fun NavKey.isOverlayRoute(): Boolean {
    return when (this) {
        is NavRoutes.Camera,
        is NavRoutes.Recorder,
        is NavRoutes.Gallery -> false

        else -> true
    }
}
