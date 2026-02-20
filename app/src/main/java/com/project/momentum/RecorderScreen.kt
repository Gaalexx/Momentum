package com.project.momentum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.momentum.ConstColours
import com.project.momentum.navigation.NavRoutes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun RecorderScreen(
    navController: NavController,
    onCameraClick: () -> Unit = {},
    onGoToFriends: () -> Unit = {}
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val mainState = remember { MainState() }

    var dragOffset by remember { mutableStateOf(0f) }
    val swipeThreshold = 80f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        dragOffset += dragAmount
                    },
                    onDragEnd = {
                        if (dragOffset < -swipeThreshold) {
                            navController.navigate(NavRoutes.Gallery)
                        }
                        dragOffset = 0f
                    },
                    onDragCancel = { dragOffset = 0f }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя панель с кнопками
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileCircleButton(
                onClick = {
                    navController.navigate(NavRoutes.Account(NavRoutes.Recorder::class.qualifiedName!!))
                },
                backgroundColor = chrome2
            )

            Spacer(Modifier.weight(1f))

            FriendsPillButton(onClick = {
                navController.navigate(NavRoutes.Friends)
            })

            Spacer(Modifier.weight(1f))

            SettingsCircleButton(
                onClick = {
                    navController.navigate(NavRoutes.Settings(NavRoutes.Recorder::class.qualifiedName!!))
                },
                backgroundColor = chrome2
            )
        }

        Spacer(Modifier.height(12.dp))

        // Основное изображение
        Box(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .aspectRatio(1.10f)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF2A2E39))
        ) {
            AsyncImage(
                model = stringResource(R.string.rec_img_model_),
                contentDescription = stringResource(R.string.recorder_main_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (mainState.currentState == "STATE_2") {
                val captionFocusRequester = remember { FocusRequester() }
                var caption by remember { mutableStateOf("") }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    CaptionBasicInput(
                        value = caption,
                        onValueChange = { caption = it },
                        placeholder = stringResource(R.string.label_write_comment),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(captionFocusRequester)
                    )
                }

                LaunchedEffect(Unit) {
                    mainState.captionFocusRequester = captionFocusRequester
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Кнопки камера/микрофон в начальном состоянии
        if (mainState.currentState == "INITIAL") {
            Row(
                modifier = Modifier.padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleButton(
                    size = 60.dp,
                    onClick = { navController.navigate(NavRoutes.Camera) },
                    icon = Icons.Outlined.PhotoCamera,
                    iconColor = ConstColours.WHITE,
                    backgroundColor = ConstColours.BLACK,
                    enabled = true
                )

                CircleButton(
                    size = 60.dp,
                    onClick = {},
                    icon = Icons.Outlined.Mic,
                    backgroundColor = ConstColours.BLACK,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Нижняя секция с микрофоном и состояниями
        SecondaryImagesSection(
            mainState = mainState,
            navController = navController
        )
    }
}

class MainState {
    var currentState by mutableStateOf("INITIAL")
    var captionFocusRequester: FocusRequester? by mutableStateOf(null)
}

@Composable
fun SecondaryImagesSection(
    mainState: MainState,
    navController: NavController
) {
    var isImage1Tinted by remember { mutableStateOf(false) }
    var isImage2Visible by remember { mutableStateOf(true) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) }
    var fixedTime by remember { mutableStateOf<Long?>(null) }
    var firstClickTime by remember { mutableStateOf<Long?>(null) }

    val captionFocusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    val iconTint = Color(0xFFEDEEF2)
    var showKeyboardTrigger by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(showKeyboardTrigger) {
        if (showKeyboardTrigger) {
            delay(100)

            val focusReq = mainState.captionFocusRequester
            if (focusReq != null) {
                repeat(3) { attempt ->
                    focusReq.requestFocus()
                    delay(50L * (attempt + 1))
                    keyboardController?.show()
                }
            } else {
                keyboardController?.show()
            }

            showKeyboardTrigger = false
        }
    }

    LaunchedEffect(Unit) {
        mainState.captionFocusRequester = captionFocusRequester
    }

    LaunchedEffect(clickCount, isImage1Tinted) {
        val newState = when {
            clickCount == 0 -> "INITIAL"
            clickCount == 1 && isImage1Tinted -> "STATE_1"
            clickCount == 2 -> "STATE_2"
            else -> "INITIAL"
        }
        mainState.currentState = newState

        if (newState == "STATE_2") {
            delay(100)
            captionFocusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    fun resetToInitialState() {
        isImage1Tinted = false
        isImage2Visible = true
        elapsedTime = 0L
        clickCount = 0
        fixedTime = null
        firstClickTime = null
        timerJob?.cancel()
        timerJob = null
        keyboardController?.hide()
    }

    val currentState = when {
        clickCount == 0 -> "INITIAL"
        clickCount == 1 && isImage1Tinted -> "STATE_1"
        clickCount == 2 -> "STATE_2"
        else -> "INITIAL"
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 23.dp)
    ) {
        if (currentState == "STATE_2") {
            timerJob?.cancel()

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                fixedTime?.let { time ->
                    Text(
                        text = stringResource(
                            R.string.recorder_duration_label,
                            formatElapsedTime(time)
                        ),
                        color = Color.Yellow,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { resetToInitialState() },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                modifier = Modifier.size(40.dp),
                                contentDescription = stringResource(R.string.recorder_reset_content_description),
                                tint = iconTint
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        BigCircleSendPhotoAction(
                            onClick = { resetToInitialState() }
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(
                            onClick = { showKeyboardTrigger = true },
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { showKeyboardTrigger = true }
                        ) {
                            Icon(
                                Icons.Outlined.TextFields,
                                modifier = Modifier.size(40.dp),
                                contentDescription = stringResource(R.string.recorder_show_keyboard_content_description),
                                tint = iconTint
                            )
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.recorder_more_content_description),
                    tint = iconTint.copy(alpha = 0.9f),
                    modifier = Modifier.size(34.dp)
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (currentState == "STATE_1") Spacer(Modifier.height(63.dp))

                val isRecording = currentState == "STATE_1"
                BigCircleMicroButton(
                    onClick = {
                        val currentTime = System.currentTimeMillis()
                        clickCount++

                        when (currentState) {
                            "INITIAL" -> {
                                isImage1Tinted = true
                                isImage2Visible = false
                                firstClickTime = currentTime

                                timerJob?.cancel()
                                timerJob = scope.launch {
                                    val startTime = currentTime
                                    while (isActive && clickCount == 1) {
                                        elapsedTime = System.currentTimeMillis() - startTime
                                        delay(100)
                                    }
                                }
                            }

                            "STATE_1" -> {
                                firstClickTime?.let { firstTime ->
                                    fixedTime = currentTime - firstTime
                                }

                                isImage1Tinted = false
                                elapsedTime = 0L
                            }
                        }
                    },
                    modifier = Modifier
                        .width(132.dp)
                        .height(132.dp),
                    isRecording = isRecording
                )

                if (currentState == "STATE_1") {
                    Text(
                        text = formatElapsedTime(elapsedTime),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            if (isImage2Visible && currentState == "INITIAL") {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = 35.dp)
                        .padding(bottom = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.recorder_more_content_description),
                        tint = Color(0xFFEDEEF2).copy(alpha = 0.65f),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
    }
}

private fun formatElapsedTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val millis = (milliseconds % 1000) / 10

    return if (minutes > 0) {
        String.format("%d:%02d:%02d", minutes, seconds, millis)
    } else {
        String.format("%02d:%02d", seconds, millis)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
fun RecorderScreenPreview() {
    RecorderScreen(
        navController = rememberNavController()
    )
}