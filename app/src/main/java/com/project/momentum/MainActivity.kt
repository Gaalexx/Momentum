package com.project.momentum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import com.project.momentum.ui.theme.MomentumTheme
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    fun previewRoute(uriEncoded: String) = "preview/$uriEncoded"
}


@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.CAMERA
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

        composable(Routes.RECORDER){
            Frame75()
        }

        composable(Routes.ACCOUNT) {
            AccountScreen(
                onPostClick = { postId -> /* Обработка клика по посту */ },
                onProfileClick = { /* Обработка профиля */ },
                onBackClick = { // ← Добавьте этот параметр
                    navController.popBackStack() // Возврат на предыдущий экран
                }
            )
        }

        composable(Routes.GALLERY) {
            GallaryScreen(
                onPostClick = { postId ->
                    // Обработка клика по посту (можно оставить пустым)
                },
                onProfileClick = {
                    navController.navigate(Routes.ACCOUNT)
                },
                onBackClick = {
                    navController.popBackStack() // Возврат на камеру
                }
            )
        }
    }
}
