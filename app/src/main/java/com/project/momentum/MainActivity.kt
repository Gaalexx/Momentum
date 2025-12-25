package com.project.momentum

import WatchPhotoScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
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

    const val ACCOUNT = "account"

    const val GALLERY = "gallery"
    const val SETTINGS = "settings"
    const val SETTINGS_WITH_BACK = "settings/{backTo}"
    const val PREVIEW_PHOTO_WITH_ARG = "previewphoto/{url}"
    const val PREVIEW_PHOTO = "previewphoto"
    fun previewRoute(uriEncoded: String) = "preview/$uriEncoded"
    fun settingsRoute(backTo: String) = "settings/$backTo"
}


@Composable
fun AppNav() {
    val navController = rememberNavController()

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
                    navController.navigate(Routes.ACCOUNT)
                },
                onOpenGallery = {
                    navController.navigate(Routes.GALLERY)
                },
                onGoToSettings = {
                    navController.navigate(Routes.settingsRoute(Routes.CAMERA))
                }
            )
        }

        composable(Routes.RECORDER) {
            RecorderScreen(
                navController = navController,
                onCameraClick = {
                    navController.navigate(Routes.CAMERA)
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
                onGoToTakePhoto = { navController.navigate(Routes.CAMERA) }
            )
        }

        composable(Routes.ACCOUNT) {
            AccountScreen(
                onPostClick = { postId -> /* Обработка клика по посту */ },
                onProfileClick = { /* Обработка профиля */ },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.GALLERY) {
            GallaryScreen(
                onPostClick = { postId ->
                    val encoded = java.net.URLEncoder.encode(postId, "UTF-8")
                    navController.navigate("previewphoto/$encoded")
                },
                onProfileClick = {
                    navController.navigate(Routes.ACCOUNT)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onGoToSettings = {
                    navController.navigate(Routes.settingsRoute(Routes.GALLERY))
                }
            )
        }


        composable(
            route = Routes.PREVIEW_PHOTO_WITH_ARG,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("url")
            val url = encoded?.let { java.net.URLDecoder.decode(it, "UTF-8") }

            WatchPhotoScreen(
                url = url,
                onGoToTakePhoto = {
                    navController.navigate(Routes.CAMERA)
                },
                onGoToGallery = {
                    navController.navigate(Routes.GALLERY)
                }
            )
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
    }
}