package com.project.momentum.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Composable
fun FriendRequestCard(
    userName: String,
    userAvatarUrl: String?,
    widthDp: Dp = 250.dp,
    heightDp: Dp = 100.dp
) {
    Box(
        modifier = Modifier
            .width(widthDp)
            .height(heightDp)
            .background(ConstColours.MAIN_BACK_GRAY)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .aspectRatio(1.0f)
                    .clip(CircleShape)
                    .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
                    .align(Alignment.CenterVertically)
            ) {
                if (userAvatarUrl == null) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.account_avatar),
                        tint = ConstColours.ACCOUNT_LOGO_TINT,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                } else {
                    AsyncImage(
                        model = userAvatarUrl,
                        contentDescription = stringResource(R.string.account_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = userName,
                        color = ConstColours.WHITE,
                        style = AppTextStyles.SupportingText
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize()
                ){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.85f),
                            colors = ButtonColors(
                                containerColor = ConstColours.MAIN_BRAND_BLUE,
                                contentColor = ConstColours.WHITE,
                                disabledContentColor = ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                                disabledContainerColor = ConstColours.WHITE)
                            ){
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = ConstColours.WHITE
                                )
                            }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.85f),
                            colors = ButtonColors(
                                containerColor = ConstColours.RED,
                                contentColor = ConstColours.WHITE,
                                disabledContentColor = ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                                disabledContainerColor = ConstColours.WHITE)
                        ){
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = ConstColours.WHITE
                            )
                        }

                    }
                }

            }

        }

    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewCard() {
    Box(contentAlignment = Alignment.Center) {
        FriendRequestCard(userName = "Preview name", userAvatarUrl = null, 200.dp, 75.dp)
    }
}
