package com.project.momentum

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import kotlinx.coroutines.*

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.momentum.ConstColours
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.TextFields

import androidx.navigation.NavController
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.ui.theme.MomentumTheme

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource

class Frame75Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MomentumTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ConstColours.BLACK)
                ) {
                    AppNav()
                }
            }
        }
    }
}

@Composable
fun RecorderScreen(
    navController: NavController? = null,
    onCameraClick: () -> Unit = {}
) {
    val bg = ConstColours.BLACK
    val iconTint = ConstColours.WHITE
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
                            navController?.navigate(Routes.GALLERY)
                        }
                        dragOffset = 0f
                    },
                    onDragCancel = { dragOffset = 0f }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileCircleButton(
                onClick = {
                    navController?.navigate(Routes.ACCOUNT_WITH_BACK)
                },
                backgroundColor = chrome2
            )

            Spacer(Modifier.weight(1f))

            FriendsPillButton(
                onClick = {}
            )

            Spacer(Modifier.weight(1f))

            SettingsCircleButton(
                onClick = {
                    navController?.navigate(Routes.settingsRoute(Routes.RECORDER))
                },
                backgroundColor = chrome2
            )
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .aspectRatio(1.10f)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF2A2E39))
        ) {
            AsyncImage(
                model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5b2573b6-2575-48f4-b8b4-8d7756ecd5b7",
                contentDescription = "Основное изображение",
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
                        placeholder = "Введите комментарий...",
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

        if (mainState.currentState == "INITIAL") {
            Row(
                modifier = Modifier.padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleButton(
                    size = 60.dp,
                    onClick = {
                        navController?.navigate(Routes.CAMERA)
                    },
                    icon = Icons.Outlined.PhotoCamera,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )

                CircleButton(
                    size = 60.dp,
                    onClick = { println("Кнопка микрофона нажата") },
                    icon = Icons.Outlined.Mic,
                    backgroundColor = ConstColours.BLACK,
                    iconColor = ConstColours.WHITE,
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))



        SecondaryImagesSection(mainState = mainState)
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun CameraLikeScreenPreview() {
    MaterialTheme {
        CameraLikeScreen(
            previewPainter = null,
            onGoToPreview = {},
            onGoToRecorder = {},
            onProfileClick = {},
            onOpenGallery = {},
            onGoToSettings = {},
            onGoToFriends = {}
        )
    }
}


class MainState {
    var currentState by mutableStateOf("INITIAL")
    var captionFocusRequester: FocusRequester? by mutableStateOf(null)
}

@Composable
fun SecondaryImagesSection(mainState: MainState) {

    var isImage1Tinted by remember { mutableStateOf(false) }
    var isImage2Visible by remember { mutableStateOf(true) }
    var lastClickTime by remember { mutableStateOf<Long?>(null) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) }
    var fixedTime by remember { mutableStateOf<Long?>(null) }
    var firstClickTime by remember { mutableStateOf<Long?>(null) }
    var caption by remember { mutableStateOf("") }

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
        lastClickTime = null
        elapsedTime = 0L
        clickCount = 0
        fixedTime = null
        firstClickTime = null
        timerJob?.cancel()
        timerJob = null
        caption = ""
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fixedTime?.let { time ->
                    Text(
                        text = "Длительность: ${formatElapsedTime(time)}",
                        color = Color.Yellow,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                resetToInitialState()
                                println("Кнопка сброса нажата")
                            },
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Сбросить",
                                tint = iconTint
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        BigCircleSendPhotoAction(
                            onClick = {
                                println("Основное действие выполнено")
                                resetToInitialState()
                            }
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                showKeyboardTrigger = true
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { showKeyboardTrigger = true }
                        ) {
                            Icon(
                                Icons.Outlined.TextFields,
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Показать клавиатуру",
                                tint = iconTint
                            )
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Ещё",
                    tint = iconTint.copy(alpha = 0.9f),
                    modifier = Modifier.size(34.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(/*bottom = 20.dp*/),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                if (currentState == "STATE_1")
                    Spacer(Modifier.height(63.dp))

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
                                lastClickTime = currentTime

                                timerJob?.cancel()
                                timerJob = scope.launch {
                                    val startTime = currentTime
                                    while (isActive && clickCount == 1) {
                                        elapsedTime = System.currentTimeMillis() - startTime
                                        delay(100)
                                    }
                                }
                                println("Первое нажатие - состояние 1")
                            }

                            "STATE_1" -> {

                                firstClickTime?.let { firstTime ->
                                    fixedTime = currentTime - firstTime
                                }

                                isImage1Tinted = false
                                lastClickTime = null
                                elapsedTime = 0L

                                println("Второе нажатие - состояние 2. Разница: ${fixedTime}ms")
                            }
                        }
                    },
                    modifier = Modifier
                        .width(132.dp)
                        .height(132.dp),
                    isRecording = isRecording
                )

                when (currentState) {
                    "STATE_1" -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatElapsedTime(elapsedTime),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            /*Text(
                            text = "таймер работает",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )*/

                            /*
                            Text(
                                text = "Нажмите снова для перехода в состояние 2",
                                color = Color(0xFF4CAF50),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 12.dp)
                                    .padding(horizontal = 16.dp)
                                    .background(
                                        color = Color(0x334CAF50),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )*/
                        }
                    }

                    "INITIAL" -> {
                        /*Text(
                            text = "Нажмите для начала",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )*/
                    }
                }
            }

            if (isImage2Visible && currentState == "INITIAL") {
                IconButton(
                    onClick = {
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = 35.dp)
                        .padding(bottom = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = "Ещё",
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
fun RecorderScreenPreview(
    navController: NavController? = null,
    onCameraClick: () -> Unit = {}
) {
    RecorderScreen()
}