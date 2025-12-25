package com.project.momentum

import WatchPhotoScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import com.project.momentum.ui.theme.MomentumTheme
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MomentumTheme {
                AppNav()
            }
        }
    }
}


object Routes {
    const val CAMERA = "camera"

    const val PREVIEW = "preview"
    const val PREVIEW_WITH_ARG = "preview/{uri}"

    const val RECORDER = "recorder"

    //const val ACCOUNT = "account"
    const val ACCOUNT_WITH_BACK = "account/{backTo}"
    const val GALLERY = "gallery"

    const val FRIENDS = "friends"

    //const val SETTINGS = "settings"
    const val SETTINGS_WITH_BACK = "settings/{backTo}"
    const val PREVIEW_PHOTO_WITH_ARG = "previewphoto/{url}"

    //const val PREVIEW_PHOTO = "previewphoto"
    fun previewRoute(uriEncoded: String) = "preview/$uriEncoded"
    fun accountRoute(backTo: String) = "account/$backTo"
    fun settingsRoute(backTo: String) = "settings/$backTo"
    fun previewPhotoRoute(urlEncoded: String) = "previewphoto/$urlEncoded"
}


@Composable
fun AppNav() {
    val navController = rememberNavController()
    val accountVm: AccountViewModel = viewModel()
    val galleryVM: GalleryViewModel = viewModel()
    val friendsVM: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.CAMERA,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        composable(Routes.CAMERA) {
            CameraLikeScreen(
                onGoToPreview = { uri ->
                    val encoded = java.net.URLEncoder.encode(uri.toString(), "UTF-8")
                    navController.navigate(Routes.previewRoute(encoded))
                },
                onGoToRecorder = {
                    navController.navigate(Routes.RECORDER)
                },
                onProfileClick = {
                    navController.navigate(Routes.accountRoute(Routes.CAMERA))
                },
                onOpenGallery = {
                    navController.navigate(Routes.GALLERY)
                },
                onGoToSettings = {
                    navController.navigate(Routes.settingsRoute(Routes.CAMERA))
                },
                onGoToFriends = {
                    navController.navigate(Routes.FRIENDS)
                }
            )
        }

        composable(Routes.PREVIEW_WITH_ARG) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("uri")
            val uri = encoded
                ?.let { java.net.URLDecoder.decode(it, "UTF-8") }
                ?.let { android.net.Uri.parse(it) }

            SendPhotoScreen(
                uri = uri,
                onGoToTakePhoto = { navController.navigate(Routes.CAMERA) },
                onGoToSettings = { navController.navigate(Routes.SETTINGS_WITH_BACK) },
                onProfileClick = { navController.navigate(Routes.ACCOUNT_WITH_BACK) },
                onGoToFriends = { navController.navigate(Routes.FRIENDS) }
            )
        }

        composable(Routes.RECORDER) {
            RecorderScreen(
                navController = navController,
                onCameraClick = {
                    navController.navigate(Routes.CAMERA)
                },
                onGoToFriends = { navController.navigate(Routes.FRIENDS) }
            )
        }

        composable(
            route = Routes.ACCOUNT_WITH_BACK,
            arguments = listOf(navArgument("backTo") { type = NavType.StringType })
        )
        { backStackEntry ->
            val backTo = backStackEntry.arguments?.getString("backTo") ?: Routes.CAMERA

            AccountScreen(
                onPostClick = {
                    accountVm.selectedPost?.let { post ->
                        navController.navigate(
                            Routes.previewPhotoRoute(
                                java.net.URLEncoder.encode(post.url, "UTF-8")
                            )
                        )
                    }
                },
                onProfileClick = { /* Обработка профиля */ },
                onBackClick = {
                    navController.navigate(Routes.CAMERA)
                },
                viewModel = accountVm
            )
        }

        composable(Routes.GALLERY) {
            GallaryScreen(
                onPostClick = { url ->
                    // Получаем выбранный пост из ViewModel галереи
                    galleryVM.selectedPost?.let { post ->
                        navController.navigate(
                            Routes.previewPhotoRoute(
                                java.net.URLEncoder.encode(post.url, "UTF-8")
                            )
                        )
                    }
                },
                onProfileClick = {
                    navController.navigate(Routes.accountRoute(Routes.GALLERY))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onGoToSettings = {
                    navController.navigate(Routes.settingsRoute(Routes.GALLERY))
                },
                onGoToFriends = {
                    navController.navigate(Routes.FRIENDS)
                },
                viewModel = galleryVM
            )
        }

        composable(
            Routes.PREVIEW_PHOTO_WITH_ARG,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val url = java.net.URLDecoder.decode(encodedUrl, "UTF-8")

            // Ищем пост по URL во всех ViewModel
            val post = accountVm.posts.find { it.url == url }
                ?: galleryVM.posts.find { it.url == url }

            post?.let {
                WatchPhotoScreen(
                    onGoToTakePhoto = { navController.navigate(Routes.CAMERA) },
                    onGoToGallery = { navController.navigate(Routes.GALLERY) },
                    onGoToSettings = { navController.navigate(Routes.SETTINGS_WITH_BACK) },
                    onProfileClick = { navController.navigate(Routes.ACCOUNT_WITH_BACK) },
                    onGoToFriends = { navController.navigate(Routes.FRIENDS) },
                    url = it.url,
                    description = it.description,
                    userName = it.name,
                    date = it.date
                )
            }
        }


        composable(
            route = Routes.SETTINGS_WITH_BACK,
            arguments = listOf(navArgument("backTo") { type = NavType.StringType })
        ) { backStackEntry ->
            val backTo = backStackEntry.arguments?.getString("backTo") ?: Routes.CAMERA

            SettingsMainScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPrivacyClick = {
                },
                onNotificationsClick = {
                },
                onDataClick = {
                },
                onLanguageClick = {
                },
                onPremiumClick = {
                },
                onLogoutClick = {
                    navController.popBackStack(Routes.CAMERA, false)
                },
                onDeleteAccountClick = {
                }
            )
        }

        composable(route = Routes.FRIENDS) {
            val users by friendsVM.users

            FriendsScreen(
                user = users.first(),
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = friendsVM
            )
        }
    }
}
