@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.ui.screens.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours

import android.content.Context
import android.net.Uri
import coil.compose.AsyncImage
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.project.momentum.R
import com.project.momentum.ui.assets.BigCircleSendPhotoAction
import com.project.momentum.ui.assets.CaptionBasicInput
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton


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
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    uri: Uri?
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
            ProfileCircleButton(onClick = onProfileClick)

            Spacer(Modifier.weight(1f))
            FriendsPillButton(onClick = onGoToFriends)
            Spacer(Modifier.weight(1f))

            SettingsCircleButton(onClick = onGoToSettings)
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(60.dp))
                .background(ConstColours.BLACK)
        ) {
            if (hasCameraPermission) {
                Box(Modifier.fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color(0xFF2A2E39))
                    .align(Alignment.Center)) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    CaptionBasicInput(
                        caption,
                        { caption = it },
                        placeholder = stringResource(R.string.label_write_comment),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .focusRequester(captionFocusRequester)
                    )


                }

            } else {
                Box(Modifier.fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color(0xFF2A2E39))
                    .align(Alignment.Center)) {
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
                    if (uri != null) {
                        deleteByUri(context = context, uri = uri)
                    }
                    onGoToTakePhoto()
                }, modifier = Modifier.size(50.dp)) {
                    Icon(
                        Icons.Default.Cancel,
                        modifier = Modifier.size(40.dp),
                        contentDescription = stringResource(R.string.icon_flash),
                        tint = iconTint
                    )

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
                    Icon(
                        Icons.Outlined.TextFields,
                        modifier = Modifier.size(40.dp),
                        contentDescription = stringResource(R.string.icon_flip_camera),
                        tint = iconTint
                    )
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
        SendPhotoScreen(
            previewPainter = null,
            onGoToTakePhoto = {},
            onProfileClick = {},
            onGoToSettings = {},
            onGoToFriends = {},
            uri = null)
    }
}
