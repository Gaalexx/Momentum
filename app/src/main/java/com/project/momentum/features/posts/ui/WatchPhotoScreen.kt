package com.project.momentum.features.posts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.momentum.ui.theme.ConstColours
//import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.assets.BigCircleForMainScreenAction
import com.project.momentum.ui.assets.FriendsPillButton
import com.project.momentum.ui.assets.ProfileCircleButton
import com.project.momentum.ui.assets.SettingsCircleButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import com.project.momentum.R
import com.project.momentum.ui.assets.CaptionBasicLabel
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.ui.theme.AppTextStyles


@Composable
fun ProfileLabel(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(70.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(ConstColours.MAIN_BACK_GRAY)
            .padding(start = 5.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(start = 7.dp)
                .fillMaxWidth()
                .size(70.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
                    .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize()
                )
            }


            Spacer(Modifier.width(13.dp))

            Text(
                text = name,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                style = AppTextStyles.MainText
            )
        }
    }

}

@Composable
fun ReactToPhoto(
    modifier: Modifier = Modifier,
    onReact: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 53.dp, start = 26.dp, end = 26.dp, top = 7.dp)
            .clip(shape = RoundedCornerShape(25.dp))
            .fillMaxWidth()
            .background(
                color = ConstColours.MAIN_BACK_GRAY,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(vertical = 7.dp, horizontal = 17.dp)
    ) {
        Text(
            stringResource(R.string.label_write_message),
            color = ConstColours.WHITE,
            style = AppTextStyles.MainText
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {}
    }
}

@Composable
fun WatchPhotoScreen(
    previewPainter: Painter? = null,
    modifier: Modifier = Modifier,
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    postUrl: String
) {
    val bg = ConstColours.BLACK
    val iconTint = ConstColours.WHITE

    val context = LocalContext.current
    var caption by rememberSaveable { mutableStateOf("") }
    val captionFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(60.dp))
                .background(ConstColours.MAIN_BACK_GRAY)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = postUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                CaptionBasicLabel(
                    text = "Description is not implemented yet",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(captionFocusRequester)
                )
            }
        }

        Spacer(Modifier.height(5.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Date is not implemented yet",
            color = ConstColours.WHITE,
            style = AppTextStyles.SupportingText
        )
        Spacer(Modifier.height(5.dp))

        ProfileLabel(
            name = "Name is not implemented yet",
            imageUrl = stringResource(R.string.cats_url)
        )

        //ReactToPhoto(onReact = {})

        //Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
                    .align(Alignment.CenterStart),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onGoToGallery,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            Icons.Default.Photo,
                            modifier = Modifier.size(40.dp),
                            contentDescription = stringResource(R.string.icon_flash),
                            tint = iconTint
                        )
                    }


//                BigCircleForMainScreenAction(
//                    onClick = onGoToTakePhoto,
//                    onLongPressStart = onStartRecordVideo,
//                    onLongPressEnd = onStopRecordVideo
//                )

                    Spacer(Modifier.weight(1f))
                    ContinueButton(onClick = onGoToTakePhoto,
                        modifier = Modifier
                            .width(200.dp)
                            .height(75.dp),
                        "Ответить",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ConstColours.MAIN_BRAND_BLUE,
                            contentColor = ConstColours.WHITE
                        )
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
                            Icons.Outlined.MoreHoriz,
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
                contentDescription = stringResource(R.string.icon_more),
                tint = iconTint.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(34.dp)
                    .align(Alignment.BottomCenter)
            )
        }


    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun WatchPhotoScreenPreview() {
    MaterialTheme {
        WatchPhotoScreen(
            previewPainter = null,
            onGoToTakePhoto = {},
            onGoToGallery = {},
            onGoToSettings = {},
            onProfileClick = {},
            onGoToFriends = {},
            postUrl = stringResource(R.string.cats_url)
        )
    }
}