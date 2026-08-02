package com.project.momentum.features.posts.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.features.account.models.PostData
import com.project.momentum.features.posts.features.reactions.models.ReactionData
import com.project.momentum.features.posts.features.reactions.models.ReactionType
import com.project.momentum.features.posts.features.reactions.ui.ReactionsDialog
import com.project.momentum.features.posts.features.reactions.ui.ReactionsRow
import com.project.momentum.features.posts.viewmodel.PostsViewModel
import com.project.momentum.features.posts.viewmodel.WatchPhotoEvent
import com.project.momentum.network.s3.MediaType
import com.project.momentum.ui.assets.AudioView
import com.project.momentum.ui.assets.CaptionBasicLabel
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.project.momentum.features.newcontentcreation.ui.assets.CameraTopBar
import com.project.momentum.features.posts.features.reactions.ui.PostDialogContent
import com.project.momentum.features.posts.features.reactions.ui.ShowDialogContent
import com.project.momentum.features.posts.viewmodel.GalleryEvent
import com.project.momentum.features.posts.viewmodel.PostsState
import com.project.momentum.ui.assets.ContinueButtonAdaptive
import com.project.momentum.ui.assets.DialogInfo
import com.project.momentum.ui.assets.VideoView
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.ui.theme.MomentumTheme
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun ProfileLabel(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String?,
    height: Dp,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(height)
//            .height(100.dp)
            .clip(RoundedCornerShape(60.dp))
            .background(ConstColours.MAIN_BACK_GRAY)
            .padding(start = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(height)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .height(height * 0.9f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(1.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
            ) {
                if (!imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                } else {
                    val iconTint = Color(0xFFEDEEF2) // TODO ВЫНЕСТИ В ЦВЕТА КОГДА БУДЕТ НЕ В ПАДЛУ
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.account_avatar),
                        tint = iconTint.copy(alpha = 0.7f),
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }

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
    onReact: () -> Unit,
    modifier: Modifier = Modifier,
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
fun WatchPhotoScreenRoute(
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    postIndex: Int,
    userId: String? = null,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val uiState by postsViewModel.state.collectAsStateWithLifecycle()

    val userPosts by remember(userId) {
        if (userId != null) {
            postsViewModel.getUserPostsFlow(userId)
        } else {
            MutableStateFlow(null)
        }
    }.collectAsStateWithLifecycle()
    val posts = if (userId == null) uiState.posts else (userPosts ?: listOf())

    WatchPhotoScreenFull(
        onGoToTakePhoto = onGoToTakePhoto,
        onGoToGallery = onGoToGallery,
        onProfileClick = onProfileClick,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,

        postDialogInfo = DialogInfo.PostDialogInfo(
            onHidePost = {
                postsViewModel.onEvent(
                    GalleryEvent.OnHidePost(
                        uiState.selectedPost
                            ?: throw Exception("GalleryScreenContent:OnHidePost: Selected post is null")
                    )
                )
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            onDeletePost = {
                postsViewModel.onEvent(
                    GalleryEvent.OnDeletePost(
                        uiState.selectedPost
                            ?: throw Exception("GalleryScreenContent:OnDeletePost: Selected post is null")
                    )
                )
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            isShowingActionsDialog = uiState.isShowingActionsDialog,
            selectedPost = uiState.selectedPost
        ),

        onShowReactionDialog = {
            postsViewModel.onWatchPhotoEvent(
                WatchPhotoEvent.OnShowReactionDialogEvent(!uiState.isShowingActionsDialog)
            )
        },
        onReactionClick = { postId, reaction ->
            postsViewModel.onWatchPhotoEvent(
                WatchPhotoEvent.OnReactionClick(postId, reaction)
            )
        },
        onTranscript = { postId ->
            postsViewModel.onEvent(GalleryEvent.GetSpeechTranscription(postId))
        },
        postIndex = postIndex,
        posts = posts,
        uiState = uiState,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
fun WatchPhotoScreenFull(
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToFriends: () -> Unit,
    onTranscript: (String) -> Unit,

    postDialogInfo: DialogInfo,

    onShowReactionDialog: (String?) -> Unit,
    onReactionClick: (String, ReactionType) -> Unit,
    postIndex: Int,
    uiState: PostsState,
    posts: List<PostData>,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {

    Surface(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(ConstColours.BLACK)
            .fillMaxSize(),
        color = ConstColours.BLACK
    ) {
        Column {
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
            WatchPhotoScreen(
                onShowReactionDialog = onShowReactionDialog,
                onReactionClick = onReactionClick,
                onGoToTakePhoto = onGoToTakePhoto,
                onGoToGallery = onGoToGallery,
                onTranscript = onTranscript,
                postDialogInfo = postDialogInfo,
                postIndex = postIndex,
                posts = posts,
                currentUserId = uiState.currentUserId,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}


@Composable
fun WatchPhotoScreenRouteForMain(
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    postIndex: Int,
    userId: String? = null,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val uiState by postsViewModel.state.collectAsStateWithLifecycle()

    val posts by postsViewModel.getShownPostsFlow().collectAsStateWithLifecycle()

    WatchPhotoScreen(
        onShowReactionDialog = { postId ->
            postsViewModel.onWatchPhotoEvent(
                WatchPhotoEvent.OnShowReactionDialogEvent(!uiState.isShowingActionsDialog)
            )
            postsViewModel.onEvent(GalleryEvent.SelectPost(postId))
        },
        onReactionClick = { postId, reaction ->
            postsViewModel.onWatchPhotoEvent(
                WatchPhotoEvent.OnReactionClick(postId, reaction)
            )
        },
        onGoToTakePhoto = onGoToTakePhoto,
        onGoToGallery = onGoToGallery,
        postDialogInfo = DialogInfo.PostDialogInfo(
            onHidePost = {
                postsViewModel.onEvent(
                    GalleryEvent.OnHidePost(
                        uiState.selectedPost
                            ?: throw Exception("GalleryScreenContent:OnHidePost: Selected post is null")
                    )
                )
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            onDeletePost = {
                postsViewModel.onEvent(
                    GalleryEvent.OnDeletePost(
                        uiState.selectedPost
                            ?: throw Exception("GalleryScreenContent:OnDeletePost: Selected post is null")
                    )
                )
                postsViewModel.onEvent(GalleryEvent.OnShowActionsDialog(!uiState.isShowingActionsDialog))
                postsViewModel.onEvent(GalleryEvent.SelectPost(null))
            },
            isShowingActionsDialog = uiState.isShowingActionsDialog,
            selectedPost = uiState.selectedPost
        ),
        onTranscript = { postId ->
            postsViewModel.onEvent(GalleryEvent.GetSpeechTranscription(postId))
        },
        postIndex = postIndex,
        posts = posts,
        currentUserId = uiState.currentUserId,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
fun WatchPhotoScreen(
    onShowReactionDialog: (String?) -> Unit,
    onReactionClick: (String, ReactionType) -> Unit,
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    postDialogInfo: DialogInfo,
    onTranscript: (String) -> Unit,
    postIndex: Int,
    currentUserId: String,
    posts: List<PostData>,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val bg = ConstColours.BLACK
    val iconTint = ConstColours.WHITE
    val context = LocalContext.current
    val captionFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState(initialPage = postIndex, pageCount = { posts.size })

    var isEditable by remember { mutableStateOf(false) }
    var isTranscriptionVisible by remember { mutableStateOf(false) }
    val backgroundBlur by animateDpAsState(
        targetValue = if (isEditable) 18.dp else 0.dp,
        label = "watch_photo_background_blur"
    )
    val blurClickInteractionSource = remember { MutableInteractionSource() }

    val currentPost by remember(posts, pagerState.currentPage) {
        derivedStateOf { posts.getOrNull(pagerState.currentPage) }
    }

    LaunchedEffect(currentPost?.id) {
        isTranscriptionVisible = false
    }

    val screenHeight = LocalWindowInfo.current.containerDpSize.height
    val screenWidth = LocalWindowInfo.current.containerDpSize.width

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (posts.isEmpty()) {
            LoadingOverlay()
        } else {
            VerticalPager(
                state = pagerState,
                userScrollEnabled = !isEditable,
                modifier = Modifier
                    .heightIn(max = screenHeight * 0.5f)
                    .aspectRatio(1f)
            ) { pageIndex ->
                val post = posts[pageIndex]

                val postModifier =
                    if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                        with(sharedTransitionScope) {
                            Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(
                                    key = "person-post-${post.id}"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(750)
                                }
                            )
                        }
                    } else {
                        Modifier
                    }

                when (post.mediaType) {
                    MediaType.IMAGE -> {
                        Box(
                            modifier = postModifier
                                .fillMaxWidth(0.95f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(60.dp))
                                .background(ConstColours.MAIN_BACK_GRAY)
                                .clickable { onShowReactionDialog(post.id) }
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                                AsyncImage(
                                    model = posts[pageIndex].presignedURL,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                if (posts[pageIndex].title.isNotBlank()) {
                                    CaptionBasicLabel(
                                        text = posts[pageIndex].title,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth(0.9f)
                                            .padding(16.dp)
                                            .focusRequester(captionFocusRequester)
                                    )
                                }

                            }
                        }
                    }

                    MediaType.VIDEO -> {
                        Box(
                            modifier = postModifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .combinedClickable(
                                    onClick = { onShowReactionDialog(post.id) },
                                    onLongClick = {
                                        isEditable = !isEditable
                                        if (!isEditable) {
                                            isTranscriptionVisible = false
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            VideoView(
                                context = context,
                                uri = post.presignedURL,
                                isEditable = isEditable,
                                isPlaying = pageIndex == pagerState.currentPage
                            )

                            if (isEditable) {
                                Icon(
                                    imageVector = Icons.Default.TextFields,
                                    contentDescription = "SpeechToText",
                                    tint = ConstColours.WHITE,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 30.dp, end = 30.dp)
                                        .size(64.dp)
                                        .clickable {
                                            isTranscriptionVisible = true
                                            onTranscript(post.id)
                                        }
                                )
                            }

                            if (posts[pageIndex].title.isNotBlank()) {
                                CaptionBasicLabel(
                                    text = posts[pageIndex].title,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth(0.9f)
                                        .padding(16.dp)
                                        .focusRequester(captionFocusRequester)
                                )
                            }
                        }
                    }

                    MediaType.AUDIO -> {
                        Box(
                            modifier = postModifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .combinedClickable(
                                    onClick = { onShowReactionDialog(post.id) },
                                    onLongClick = {
                                        isEditable = !isEditable
                                        if (!isEditable) {
                                            isTranscriptionVisible = false
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            AudioView(
                                context = context,
                                uri = post.presignedURL,
                                isEditable = isEditable,
                                isPlaying = pageIndex == pagerState.currentPage
                            )

                            if (isEditable) {
                                Icon(
                                    imageVector = Icons.Default.TextFields,
                                    contentDescription = "SpeechToText",
                                    tint = ConstColours.WHITE,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 30.dp, end = 30.dp)
                                        .size(64.dp)
                                        .clickable {
                                            isTranscriptionVisible = true
                                            onTranscript(post.id)
                                        }
                                )
                            }

                            if (posts[pageIndex].title.isNotBlank()) {
                                CaptionBasicLabel(
                                    text = posts[pageIndex].title,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth(0.9f)
                                        .padding(16.dp)
                                        .focusRequester(captionFocusRequester)
                                )
                            }
                        }
                    }
                }

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(backgroundBlur)
                    .clickable(
                        enabled = isEditable,
                        interactionSource = blurClickInteractionSource,
                        indication = null
                    ) {
                        isEditable = false
                        isTranscriptionVisible = false
                    },
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                currentPost?.let { post ->
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = post.getDate() ?: "",
                        color = ConstColours.WHITE,
                        style = AppTextStyles.SupportingText
                    )

                    if (post.reactions != null) {
                        ReactionsRow(
                            curUser = currentUserId,
                            reactionsData = post.reactions,
                            onReactionClick = { reaction ->
                                onReactionClick(post.id, reaction)
                            },
                            modifier = Modifier
                                .height(65.dp)
                                .padding(8.dp)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .height(65.dp)
                                .padding(8.dp)
                        )
                    }

                    if (postDialogInfo.isShowingActionsDialog) {
                        Dialog(
                            onDismissRequest = { onShowReactionDialog(null) }
                        ) {
                            ReactionsDialog(
                                onReactionClick = { reaction ->
                                    onReactionClick(post.id, reaction)
                                    onShowReactionDialog(null)
                                },
                            ) {
                                when(postDialogInfo) {
                                    is DialogInfo.PostDialogInfo -> PostDialogContent(
                                        isOwner = post.isOwner,
                                        onHidePost = postDialogInfo.onHidePost,
                                        onDeletePost = postDialogInfo.onDeletePost,
                                    )
                                    is DialogInfo.Hidden -> ShowDialogContent(
                                        onShowPost = postDialogInfo.onShowPost
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfileLabel(
                            name = post.userName,
                            imageUrl = post.avatarPresignedURL,
                            height = screenHeight * 0.1f
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = dimensionResource(R.dimen.medium_padding),
                            top = dimensionResource(R.dimen.small_padding)
                        ),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = {
                            if (!isEditable) {
                                onGoToGallery()
                            }
                        },
                        modifier = Modifier.size(dimensionResource(R.dimen.sub_button_size))
                    ) {
                        Icon(
                            Icons.Default.Photo,
                            modifier = Modifier.size(40.dp),
                            contentDescription = stringResource(R.string.icon_flash),
                            tint = iconTint
                        )
                    }

                    ContinueButtonAdaptive(
                        onClick = {
                            if (!isEditable) {
                                onGoToTakePhoto()
                            }
                        },
                        modifier = Modifier
                            .width(screenWidth * 0.5f)
                            .height(dimensionResource(R.dimen.sub_button_size)),
                        stringResource(R.string.reply),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ConstColours.MAIN_BRAND_BLUE,
                            contentColor = ConstColours.WHITE
                        )
                    )

                    IconButton(
                        onClick = {
                            if (!isEditable) {
                                onShowReactionDialog(null)
                            }
                        },
                        modifier = Modifier.size(dimensionResource(R.dimen.sub_button_size))
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

            if (isTranscriptionVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(0.8f)
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = ConstColours.MAIN_BACK_GRAY)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 2.dp),
                            text = stringResource(R.string.transcription),
                            textAlign = TextAlign.Center,
                            color = ConstColours.WHITE
                        )
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(15.dp)
                                )
                                .background(color = ConstColours.BLACK)
                                .padding(4.dp)
                                .fillMaxSize()
                        ) {
                            if (currentPost?.transcription == null) {
                                LoadingOverlay()
                            } else {
                                Text(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .verticalScroll(state = rememberScrollState()),
                                    text = currentPost?.transcription
                                        ?: stringResource(R.string.transcripting_error),
                                    color = ConstColours.WHITE
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun WatchPhotoScreenPreview() {
    MomentumTheme {
        WatchPhotoScreenFull(
            onShowReactionDialog = {},
            onReactionClick = { _, _ -> },
            onGoToTakePhoto = {},
            onGoToGallery = {},
            onGoToSettings = {},
            onProfileClick = {},
            onGoToFriends = {},
            postDialogInfo = DialogInfo.PostDialogInfo(),
            onTranscript = { _ -> },
            postIndex = 0,
            posts = listOf(
                PostData(
                    id = "1",
                    userId = "preview-user",
                    userName = "PreviewName",
                    title = "Description",
                    presignedURL = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                    mediaType = MediaType.IMAGE,
                    isOwner = false,
                    reactions = listOf(
                        ReactionData(
                            emoji = ReactionType.HEART,
                            users = listOf("user1")
                        ),
                        ReactionData(
                            emoji = ReactionType.CLOWN,
                            users = listOf("user1", "user2", "preview-user")
                        ),
                    ),
                    createdAt = "2026-03-12T14:38:50.690942Z"
                ),
                PostData(
                    id = "2",
                    userId = "preview-user",
                    userName = "PreviewName2",
                    title = "Description2",
                    presignedURL = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                    mediaType = MediaType.IMAGE,
                    isOwner = false,
                    createdAt = "2026-03-12T14:38:50.690942Z"
                )
            ),
            uiState = PostsState(
                posts = listOf(
                    PostData(
                        id = "1",
                        userId = "preview-user",
                        userName = "PreviewName",
                        title = "Description",
                        presignedURL = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                        mediaType = MediaType.IMAGE,
                        isOwner = false,
                        reactions = listOf(
                            ReactionData(
                                emoji = ReactionType.HEART,
                                users = listOf("user1")
                            ),
                            ReactionData(
                                emoji = ReactionType.CLOWN,
                                users = listOf("user1", "user2", "preview-user")
                            ),
                        ),
                        createdAt = "2026-03-12T14:38:50.690942Z"
                    ),
                    PostData(
                        id = "2",
                        userId = "preview-user",
                        userName = "PreviewName2",
                        title = "Description2",
                        presignedURL = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                        mediaType = MediaType.IMAGE,
                        isOwner = true,
                        createdAt = "2026-03-12T14:38:50.690942Z"
                    )
                ),
                hiddenPosts = listOf(),
                isRefreshing = false,
                currentUserId = "123"
            )
        )
    }
}
