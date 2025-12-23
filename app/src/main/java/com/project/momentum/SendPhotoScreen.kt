@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.text.SimpleDateFormat
import java.util.Locale

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.net.URLDecoder
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.camera.core.Camera
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer


import androidx.camera.core.Preview as CameraXPreview


fun deleteByUri(context: Context, uri: Uri): Boolean {
    return try {
        val rows = context.contentResolver.delete(uri, null, null)
        rows > 0
    } catch (e: SecurityException) {
        false
    } catch (e: Exception) {
        false
    }
}


@Composable
fun SendPhotoScreen(
    previewPainter: Painter? = null,
    modifier: Modifier = Modifier,
    onGoToTakePhoto: () -> Unit,
    uri: android.net.Uri?
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = ConstColours.WHITE

    val context = LocalContext.current
    var caption by rememberSaveable { mutableStateOf("") }
    val captionFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var torchEnabled by remember { mutableStateOf(false) }

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val hasCameraPermission by rememberCameraPermissionState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            IconButton(
//                onClick = { },
//                modifier = Modifier
//                    .size(36.dp)
//                    .clip(CircleShape)
//                    .background(chrome2)
//                    .border(1.dp, ConstColours.MAIN_BACK_GRAY, CircleShape)
//            ) { ProfileCircleButton(onClick = {}, backgroundColor = chrome2) }
            ProfileCircleButton(onClick = {}, backgroundColor = chrome2)
            Spacer(Modifier.weight(1f))
            FriendsPillButton(onClick = {})
            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(chrome2)
                    .border(1.dp, ConstColours.MAIN_BACK_GRAY, CircleShape)
            ) { SettingsCircleButton(onClick = {}, backgroundColor = chrome2) }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .aspectRatio(1.10f)
                .clip(RoundedCornerShape(28.dp))
                .background(ConstColours.MAIN_BACK_GRAY)
        ) {
            if (hasCameraPermission) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                        contentScale = ContentScale.Crop
                     )
                    CaptionBasicInput(
                        caption,
                        {caption = it},
                        placeholder = "Введите комментарий...",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .focusRequester(captionFocusRequester)
                    )


                }

            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.35f),
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

        }




        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if(uri != null){
                        deleteByUri(context = context, uri = uri)
                    }
                    onGoToTakePhoto()
                                     }
                    , modifier = Modifier.size(50.dp)) {
                    Icon(Icons.Default.Cancel, modifier = Modifier.size(40.dp), contentDescription = "Flash", tint = iconTint)

                }

                Spacer(Modifier.weight(1f))
                BigCircleSendPhotoAction(
                    onClick = onGoToTakePhoto
                )
                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {
                        captionFocusRequester.requestFocus()
                        keyboardController?.show()
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Outlined.TextFields, modifier = Modifier.size(40.dp), contentDescription = "Flip camera", tint = iconTint)
                }
            }

        }

        Spacer(Modifier.height(15.dp))

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "More",
            tint = iconTint.copy(alpha = 0.9f),
            modifier = Modifier.size(34.dp)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun CameraLikeScreenPreview() {
    MaterialTheme {
        SendPhotoScreen(previewPainter = null, onGoToTakePhoto = {}, uri = null)
    }
}
