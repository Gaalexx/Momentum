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

class Frame75Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Сразу задаём чёрный фон для всего экрана
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Frame75()
            }
        }
    }
}

@Composable
fun Frame75() {
    // ОДИН экземпляр MainState для всего экрана
    val mainState = remember { MainState() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 14.dp,)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(bottom = 7.dp, start = 12.dp, end = 12.dp,)
                        .fillMaxWidth()
                ) {
                    // Кнопка с иконкой профиля
                    IconButton(
                        onClick = { println("Профиль нажат") },
                        modifier = Modifier
                            .size(43.dp)
                    ) {
                        AsyncImage(
                            model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/e7883c74-9a87-4678-b06f-ffd7983955d1",
                            contentDescription = "Иконка профиля",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Кнопка "Друзья"
                    Button(
                        onClick = { println("Друзья нажаты") },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        border = BorderStroke(1.dp, Color.Transparent),
                        modifier = Modifier
                            .padding(vertical = 11.dp, horizontal = 30.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/cb020208-1aca-423e-9198-4b3a57eb9c73",
                                contentDescription = "Иконка друзей",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(end = 8.dp,)
                                    .width(18.dp)
                                    .height(18.dp)
                            )
                            Text(
                                "Друзья",
                                color = Color(0xFFFFFFFF),
                                fontSize = 14.sp,
                            )
                        }
                    }

                    // Кнопка с иконкой настроек
                    IconButton(
                        onClick = { println("Настройки нажаты") },
                        modifier = Modifier
                            .size(43.dp)
                    ) {
                        AsyncImage(
                            model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/bd2b5f33-ee5f-40c6-8eb8-6bd2533b8cb9",
                            contentDescription = "Иконка настроек",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(bottom = 92.dp,)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5b2573b6-2575-48f4-b8b4-8d7756ecd5b7",
                            contentDescription = "Основное изображение",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Показываем Row с иконками только в INITIAL состоянии
                    // Используем ОДИН mainState, созданный выше
                    if (mainState.currentState == "INITIAL") {
                        // Контейнер для соприкасающихся кнопок (только в INITIAL)
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 30.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(100.dp)
                                )
                                .background(
                                    color = Color(0xFF2B2B2B),
                                    shape = RoundedCornerShape(100.dp)
                                )
                        ) {
                            // Кнопка Камера (левая часть)
                            OutlinedButton(
                                onClick = { println("Камера нажата!") },
                                border = BorderStroke(0.dp, Color.Transparent),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                                shape = RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 0,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 0
                                ),
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AsyncImage(
                                        model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a53cb3b4-72a0-44a3-8280-dd9d67336387",
                                        contentDescription = "Иконка камеры",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .padding(end = 8.dp,)
                                            .width(18.dp)
                                            .height(18.dp)
                                    )
                                    Text(
                                        "Камера",
                                        color = Color(0xFFFFFFFF),
                                        fontSize = 14.sp,
                                    )
                                }
                            }

                            // Разделитель между кнопками
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                                    .background(Color(0x60FFFFFF))
                            )

                            // Кнопка Голос (правая часть)
                            OutlinedButton(
                                onClick = { println("Голос нажата!") },
                                border = BorderStroke(0.dp, Color.Transparent),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                                shape = RoundedCornerShape(
                                    topStartPercent = 0,
                                    topEndPercent = 50,
                                    bottomStartPercent = 0,
                                    bottomEndPercent = 50
                                ),
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AsyncImage(
                                        model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5d71381a-f06f-4fdc-8e0b-23017710a7fa",
                                        contentDescription = "Иконка микрофона",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .padding(end = 8.dp,)
                                            .width(18.dp)
                                            .height(18.dp)
                                    )
                                    Text(
                                        "Голос",
                                        color = Color(0xFFFFFFFF),
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                        }
                    }
                }

                // Ряд для вторичных изображений с состоянием
                // Передаем mainState как параметр
                SecondaryImagesSection(mainState = mainState)
            }
        }
    }
}

// Класс для хранения состояния, доступного из разных композейблов
class MainState {
    var currentState by mutableStateOf("INITIAL")
}

// Отдельный композейбл для секции с вторичными изображениями
// Теперь принимает mainState как параметр
@Composable
fun SecondaryImagesSection(mainState: MainState) {
    // Состояния приложения
    var isImage1Tinted by remember { mutableStateOf(false) }
    var isImage2Visible by remember { mutableStateOf(true) }
    var lastClickTime by remember { mutableStateOf<Long?>(null) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var clickCount by remember { mutableStateOf(0) } // Счетчик нажатий
    var fixedTime by remember { mutableStateOf<Long?>(null) } // Зафиксированное время
    var firstClickTime by remember { mutableStateOf<Long?>(null) } // Время первого нажатия (состояние 1)

    // Создаем scope для управления корутинами
    val scope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    // Обновляем состояние при изменении
    LaunchedEffect(clickCount, isImage1Tinted) {
        val newState = when {
            clickCount == 0 -> "INITIAL"
            clickCount == 1 && isImage1Tinted -> "STATE_1"
            clickCount == 2 -> "STATE_2"
            else -> "INITIAL"
        }
        mainState.currentState = newState
    }

    // Функция для сброса в исходное состояние
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
    }

    // Определяем текущее состояние
    val currentState = when {
        clickCount == 0 -> "INITIAL" // Начальное состояние
        clickCount == 1 && isImage1Tinted -> "STATE_1" // Состояние 1 (красный, таймер)
        clickCount == 2 -> "STATE_2" // Состояние 2 (Row изображений)
        else -> "INITIAL" // После сброса
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 31.dp)
    ) {
        // Состояние 2: Row с изображениями
        if (currentState == "STATE_2") {
            // Останавливаем таймер
            timerJob?.cancel()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Row с тремя изображениями
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Изображение 1 - сбрасывает в изначальное состояние
                    IconButton(
                        onClick = {
                            resetToInitialState()
                            println("Изображение 1 нажато - сброс состояния")
                        },
                        modifier = Modifier.size(80.dp)
                    ) {
                        AsyncImage(
                            model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/3e4b74a5-e2fd-44b4-8afe-609117e887ed",
                            contentDescription = "Изображение 1 - сброс",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Изображение 2 - сбрасывает в изначальное состояние
                    IconButton(
                        onClick = {
                            resetToInitialState()
                            println("Изображение 2 нажато - сброс состояния")
                        },
                        modifier = Modifier.size(80.dp)
                    ) {
                        AsyncImage(
                            model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/6686c1ff-fbd2-4464-8ede-c15f7707071b",
                            contentDescription = "Изображение 2 - сброс",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Изображение 3 - ничего не делает (по вашему описанию)
                    IconButton(
                        onClick = {
                            println("Изображение 3 нажато - бездействие")
                        },
                        modifier = Modifier.size(80.dp)
                    ) {
                        AsyncImage(
                            model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/3899b413-8acb-4e21-8e0a-1551ae0f0bf9",
                            contentDescription = "Изображение 3",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Показываем разницу между нажатиями в состоянии 1 и 2
                fixedTime?.let { time ->
                    Text(
                        text = "Время реакции: ${formatElapsedTime(time)}",
                        color = Color.Yellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Разница между нажатием 1 и 2",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            // Состояние INITIAL или STATE_1
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Кнопка с вторичным изображением 1
                IconButton(
                    onClick = {
                        val currentTime = System.currentTimeMillis()
                        clickCount++

                        when (currentState) {
                            "INITIAL" -> {
                                // Первое нажатие - переход в состояние 1
                                isImage1Tinted = true
                                isImage2Visible = false
                                firstClickTime = currentTime
                                lastClickTime = currentTime

                                // Запускаем таймер
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
                                // Второе нажатие - переход в состояние 2
                                // Запоминаем разницу между нажатиями
                                firstClickTime?.let { firstTime ->
                                    fixedTime = currentTime - firstTime
                                }

                                // Сбрасываем состояние 1
                                isImage1Tinted = false
                                lastClickTime = null
                                elapsedTime = 0L

                                println("Второе нажатие - состояние 2. Разница: ${fixedTime}ms")
                            }
                        }
                    },
                    modifier = Modifier
                        .width(132.dp)
                        .height(132.dp)
                ) {
                    AsyncImage(
                        model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/616a6572-e465-41dc-9dd9-4a51ecddbf9a",
                        contentDescription = "Вторичное изображение 1",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize(),
                        colorFilter = if (isImage1Tinted) ColorFilter.tint(
                            color = Color.Red,
                            blendMode = BlendMode.SrcAtop
                        ) else null
                    )
                }

                // Показываем информацию в зависимости от состояния
                when (currentState) {
                    "STATE_1" -> {
                        // В состоянии 1 показываем таймер
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

                            Text(
                                text = "таймер работает",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            // Инструкция для пользователя
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
                            )
                        }
                    }
                    "INITIAL" -> {
                        // В начальном состоянии показываем инструкцию
                        Text(
                            text = "Нажмите для начала",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Кнопка с вторичным изображением 2 (условное отображение)
            if (isImage2Visible && currentState == "INITIAL") {
                IconButton(
                    onClick = {
                        println("Вторичное изображение 2 нажато")
                    },
                    modifier = Modifier
                        .width(113.dp)
                        .height(113.dp)
                ) {
                    AsyncImage(
                        model = "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/dd15de1e-1dfb-44c2-8380-22be766bf94d",
                        contentDescription = "Вторичное изображение 2",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

// Функция форматирования прошедшего времени
private fun formatElapsedTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val millis = (milliseconds % 1000) / 10 // сотые секунды

    return if (minutes > 0) {
        String.format("%d:%02d:%02d", minutes, seconds, millis)
    } else {
        String.format("%02d:%02d", seconds, millis)
    }
}