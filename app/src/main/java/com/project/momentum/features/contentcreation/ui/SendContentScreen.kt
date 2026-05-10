@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.momentum.features.contentcreation.ui

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.momentum.R
import com.project.momentum.features.contentcreation.models.MediaTypeToSend
import com.project.momentum.features.contentcreation.permissions.rememberCameraPermissionState
import com.project.momentum.features.contentcreation.ui.assets.CameraTopBar
import com.project.momentum.features.contentcreation.viewmodel.ContentCreationViewModel
import com.project.momentum.features.contentcreation.viewmodel.UploadEvent
import com.project.momentum.features.contentcreation.viewmodel.UploadState
import com.project.momentum.network.s3.MediaType
import com.project.momentum.network.s3.PostInformation
import com.project.momentum.ui.assets.AudioPreview
import com.project.momentum.ui.assets.BigCircleSendPhotoActionAdaptive
import com.project.momentum.ui.assets.CaptionBasicInput
import com.project.momentum.ui.assets.VideoPreview
import com.project.momentum.ui.theme.ConstColours

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import coil.request.ImageRequest
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.CircleShape
import com.project.momentum.features.friends.viewmodel.FriendsViewModel
import com.example.Models.FriendshipResponseDTO
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.unit.sp
import com.project.momentum.features.friends.ui.User
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope


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
fun SendContentScreen(
    modifier: Modifier = Modifier,
    onGoToTakePhoto: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    onError: () -> Unit,
    uri: Uri,
    mediaType: MediaTypeToSend,
    vm: ContentCreationViewModel = hiltViewModel(),
    friendsViewModel: FriendsViewModel = hiltViewModel()
) {

    val uploadState by vm.state.collectAsStateWithLifecycle()
    val uploadingState = uploadState as? UploadState.Uploading
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = uploadingState != null
    )

    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> onGoToTakePhoto()
            is UploadState.Error -> onError()
            else -> Unit
        }

    }

    val friendsScreenState by friendsViewModel.state.collectAsStateWithLifecycle()
    val friendsList = friendsScreenState.friends

    LaunchedEffect(Unit) {
        friendsViewModel.onEvent(FriendsScreenEvent.GetFriends)
    }


    val context = LocalContext.current
    var caption by rememberSaveable { mutableStateOf("") }
    val captionFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val hasCameraPermission by rememberCameraPermissionState(
        shouldRequest = mediaType != MediaTypeToSend.AUDIO
    )

    var selectedFriendIds by rememberSaveable {
        mutableStateOf<Set<String>>(emptySet())
    }
    LaunchedEffect(friendsList) {
        selectedFriendIds = friendsList.map { it.id }.toSet()
    }
    LaunchedEffect(Unit) {
        friendsViewModel.onEvent(FriendsScreenEvent.GetFriends)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(friendsList) {
        val validIds = friendsList.map { it.id }.toSet()
        if (selectedFriendIds.any { it !in validIds }) {
            selectedFriendIds = selectedFriendIds.filter { it in validIds }.toSet()
            // todo: snackbar
        }
    }

    fun toggleFriendSelection(friendId: String) {
        selectedFriendIds = if (selectedFriendIds.contains(friendId)) {
            selectedFriendIds.minus(friendId)
        } else {
            selectedFriendIds.plus(friendId)
        }
    }

    fun sendContent() {
        if (selectedFriendIds.isEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Выберите хотя бы одного друга")
            }
            return
        }

        val safeUri = uri
        val mimeType = context.contentResolver.getType(safeUri) ?: when (mediaType) {
            MediaTypeToSend.PHOTO -> context.contentResolver.getType(safeUri) ?: "image/jpeg"
            MediaTypeToSend.VIDEO -> context.contentResolver.getType(safeUri) ?: "video/mp4"
            MediaTypeToSend.AUDIO -> "audio/3gpp"
        }
        val uploadMediaType = when (mediaType) {
            MediaTypeToSend.PHOTO -> MediaType.IMAGE
            MediaTypeToSend.VIDEO -> MediaType.VIDEO
            MediaTypeToSend.AUDIO -> MediaType.AUDIO
        }
        val size = context.contentResolver.openFileDescriptor(safeUri, "r")
            ?.use { pfd -> pfd.statSize }?.takeIf { it >= 0 } ?: 0L

        vm.onEvent(
            UploadEvent.Send(
                PostInformation(
                    safeUri,
                    mimeType,
                    uploadMediaType,
                    size = size,
                    label = caption
                    //server integration
                    //,receiverIds = selectedFriendIds.toList()
                )
            )
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = ConstColours.BLACK,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ConstColours.BLACK)
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {
            CameraTopBar(
                onProfileClick = onProfileClick,
                onGoToSettings = onGoToSettings,
                onGoToFriends = onGoToFriends,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 14.dp),
            )

            Spacer(Modifier.height(5.dp))

            SendContentPreviewCard(
                hasCameraPermission = hasCameraPermission,
                mediaType = mediaType,
                uri = uri,
                caption = caption,
                onCaptionChange = { caption = it },
                captionFocusRequester = captionFocusRequester,
                isUploading = uploadingState != null,
                uploadComposition = composition,
                uploadProgress = progress,
            )

            if (uploadingState != null) {
                UploadProgress(
                    modifier = Modifier.fillMaxWidth(),
                    uploadingState = uploadingState
                )
            } else {
                Spacer(modifier = Modifier.weight(0.3f))
            }

            //Spacer(Modifier.weight(0.7f))


            if (friendsList.isNotEmpty()) {
                FriendsToShareRow(
                    friends = friendsList,
                    selectedFriendIds = selectedFriendIds,
                    onToggleFriend = { friendId -> toggleFriendSelection(friendId) }
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))

            SendContentBottomControls(
                onDelete = {
                    deleteByUri(context = context, uri = uri)
                    onGoToTakePhoto()
                },
                onSend = ::sendContent,
                onEditCaption = {
                    captionFocusRequester.requestFocus()
                    keyboardController?.show()
                },
                isSendEnabled = selectedFriendIds.isNotEmpty(),
                onSendBlocked = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Выберите хотя бы одного друга")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
            )


            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
private fun FriendsToShareRow(
    friends: List<User>,
    selectedFriendIds: Set<String>,
    onToggleFriend: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Поделиться с друзьями (${selectedFriendIds.size}/${friends.size})",
            color = ConstColours.WHITE,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(friends) { friend ->
                FriendAvatarItem(
                    friend = friend,
                    isSelected = selectedFriendIds.contains(friend.id),
                    onClick = { onToggleFriend(friend.id) }
                )
            }
        }
    }
}

@Composable
private fun FriendAvatarItem(
    friend: User,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) {
                        ConstColours.MAIN_BACK_GRAY
                    } else {
                        Color.Gray.copy(alpha = 0.3f)
                    }
                )
        ) {
            if (friend.avatarUrl != null) {
                AsyncImage(
                    model = friend.avatarUrl,
                    contentDescription = friend.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isSelected) {
                        Color.White.copy(alpha = 0.5f)
                    } else {
                        Color.White.copy(alpha = 0.2f)
                    },
                    modifier = Modifier.size(28.dp).align(Alignment.Center)
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.Green, CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Text(
            text = friend.name ?: friend.email.take(8),
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun SendContentPreviewCard(
    hasCameraPermission: Boolean,
    mediaType: MediaTypeToSend,
    uri: Uri,
    caption: String,
    onCaptionChange: (String) -> Unit,
    captionFocusRequester: FocusRequester,
    isUploading: Boolean,
    uploadComposition: LottieComposition?,
    uploadProgress: Float,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(60.dp))
            .background(ConstColours.BLACK)
    ) {
        if (hasCameraPermission || mediaType == MediaTypeToSend.AUDIO) {
            when (mediaType) {
                MediaTypeToSend.PHOTO -> {
                    Box(
                        Modifier
                            .fillMaxWidth(0.95f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(60.dp))
                            .background(ConstColours.MAIN_BACK_GRAY)
                            .align(Alignment.Center)
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                MediaTypeToSend.VIDEO -> {
                    VideoPreview(context = context, uri = uri)
                }

                MediaTypeToSend.AUDIO -> {
                    AudioPreview(context = context, uri = uri)
                }
            }
        } else {
            Box(
                Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color(0xFF2A2E39))
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        CaptionBasicInput(
            caption,
            onCaptionChange,
            placeholder = stringResource(R.string.label_write_comment),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(captionFocusRequester)
        )

        if (isUploading) {
            LottieAnimation(
                composition = uploadComposition,
                progress = { uploadProgress },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun SendContentBottomControls(
    onDelete: () -> Unit,
    onSend: () -> Unit,
    onEditCaption: () -> Unit,
    isSendEnabled: Boolean,
    onSendBlocked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            Icon(
                imageVector = Icons.Default.Cancel,
                modifier = Modifier.fillMaxSize(0.8f),
                contentDescription = stringResource(R.string.icon_flash),
                tint = ConstColours.WHITE
            )
        }

        Box(
            modifier = Modifier.weight(1.5f),
            contentAlignment = Alignment.Center
        ) {
            BigCircleSendPhotoActionAdaptive(
                onClick = if (isSendEnabled) onSend else onSendBlocked,
                modifier = Modifier.aspectRatio(1f),
                enabled = true
            )
        }

        IconButton(
            onClick = onEditCaption,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            Icon(
                imageVector = Icons.Outlined.TextFields,
                modifier = Modifier.fillMaxSize(0.8f),
                contentDescription = stringResource(R.string.icon_flip_camera),
                tint = ConstColours.WHITE
            )
        }
    }
}

@Composable
private fun UploadProgress(modifier: Modifier = Modifier, uploadingState: UploadState.Uploading?) {
    if (uploadingState == null) {
        Spacer(modifier = modifier)
    } else {
        val progress = uploadingState.progress

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (progress != null) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "$progress%",
                    color = ConstColours.WHITE
                )
            } else {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}


@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun CameraLikeScreenPreview() {
    MaterialTheme {
        SendContentScreen(
            onGoToTakePhoto = {},
            onProfileClick = {},
            onGoToSettings = {},
            onError = {},
            onGoToFriends = {},
            mediaType = MediaTypeToSend.VIDEO,
            uri = Uri.parse("https://avatars.mds.yandex.net/i?id=bd0db579c3e6b8b77e497c3185128489_l-13017849-images-thumbs&n=13")
        )
    }
}
