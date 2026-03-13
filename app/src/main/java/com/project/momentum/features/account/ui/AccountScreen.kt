@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.features.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.momentum.ui.theme.ConstColours
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.project.momentum.R
import com.project.momentum.ui.assets.S3PhotoGrid
import com.project.momentum.ui.assets.BackCircleButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.project.momentum.features.account.viewmodel.AccountInfoState
import com.project.momentum.features.account.viewmodel.AccountInfoViewModel
import com.project.momentum.features.account.viewmodel.AccountMediaEvent
import com.project.momentum.features.account.viewmodel.AccountViewModel
import com.project.momentum.features.account.viewmodel.MediaState


@Composable
fun AccountRoot(
    modifier: Modifier = Modifier,
    userStatus: String = stringResource(R.string.account_online_status),
    onPostClick: (String) -> Unit,
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onAddPostClick: () -> Unit,
    accountMediaViewModel: AccountViewModel = hiltViewModel(),
    accountInfoViewModel: AccountInfoViewModel = hiltViewModel()
) {
    val uiInfoState by accountInfoViewModel.state.collectAsStateWithLifecycle()
    val uiMediaState by accountMediaViewModel.state.collectAsStateWithLifecycle()

    AccountScreen(
        modifier = modifier,
        userStatus = userStatus,
        onBackClick = onBackClick,
        onAddPostClick = onAddPostClick,
        onPostClick = onPostClick,
        uiInfoState = uiInfoState,
        uiMediaState = uiMediaState
    )
}

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    userStatus: String = stringResource(R.string.account_online_status),
    onPostClick: (String) -> Unit,
    onProfileClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onAddPostClick: () -> Unit,
    uiInfoState: AccountInfoState,
    uiMediaState: MediaState
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = Color(0xFFEDEEF2)
    val textColor = Color.White
    val context: Context = LocalContext.current



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackCircleButton(
                onClick = onBackClick
            )
        }

        Spacer(Modifier.height(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(chrome2)
                    .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
            ) {
                if (uiInfoState.profilePhotoURL == null) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.account_avatar),
                        tint = iconTint.copy(alpha = 0.7f),
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                    )
                } else {
                    AsyncImage(
                        model = uiInfoState.profilePhotoURL,
                        contentDescription = stringResource(R.string.account_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = uiInfoState.name,
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = userStatus,
                    color = Color(0xFFA0A0A0),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(chrome2.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.account_my_publications),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )

            S3PhotoGrid(
                posts = uiMediaState.posts,
                onPostClick = onPostClick,
                onAddPhotoClick = onAddPostClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showPlusButton = true,
                columns = 3
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun AccountScreenPreview() {
    MaterialTheme {
        AccountScreen(
            onPostClick = {},
            onProfileClick = {},
            onBackClick = {},
            onAddPostClick = {},
            uiInfoState = AccountInfoState("Preview", null),
            uiMediaState = MediaState(listOf())
        )
    }
}
