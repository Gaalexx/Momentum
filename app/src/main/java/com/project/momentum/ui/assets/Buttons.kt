package com.project.momentum.ui.assets


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.project.momentum.ui.theme.AppTextStyles
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import com.project.momentum.R
import com.project.momentum.features.settings.ui.SubscriptionOption
import com.project.momentum.ui.theme.ConstColours


@Composable
fun BackCircleButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButtonAdaptive(
        onClick = onClick,
        icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
        contentDescription = "Back",
        iconScale = 0.55f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun BackCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButton(
        onClick = onClick,
        icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
        contentDescription = "Back",
        size = size,
        iconSize = size * 0.55f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun AddFriendCircleButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButtonAdaptive(
        onClick = onClick,
        icon = Icons.Outlined.PersonAdd,
        contentDescription = "Add friend",
        iconScale = 0.55f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun AddFriendCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButton(
        onClick = onClick,
        icon = Icons.Outlined.PersonAdd,
        contentDescription = "Add friend",
        size = size,
        iconSize = size * 0.55f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun ProfileCircleButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButtonAdaptive(
        onClick = onClick,
        icon = Icons.Outlined.AccountCircle,
        contentDescription = "Profile",
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun ProfileCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButton(
        onClick = onClick,
        icon = Icons.Outlined.AccountCircle,
        contentDescription = "Profile",
        size = size,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun EditCircleButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButtonAdaptive(
        onClick = onClick,
        icon = Icons.Outlined.Edit,
        contentDescription = "Edit profile",
        iconScale = 0.5f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun EditCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButton(
        onClick = onClick,
        icon = Icons.Outlined.Edit,
        contentDescription = "Edit profile",
        size = size,
        iconSize = size * 0.5f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun SettingsCircleButtonAdaptive(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
    filledIcon: Boolean = true,
) {
    val icon = if (filledIcon) Icons.Filled.Settings else Icons.Outlined.Settings

    CircleButtonAdaptive(
        modifier = modifier,
        onClick = onClick,
        icon = icon,
        contentDescription = "Settings",
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled
    )
}

@Composable
fun SettingsCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
    filledIcon: Boolean = true,
) {
    val icon = if (filledIcon) Icons.Filled.Settings else Icons.Outlined.Settings

    CircleButton(
        onClick = onClick,
        icon = icon,
        contentDescription = "Settings",
        size = size,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 50.dp,
    iconSize: Dp = size * 0.62f,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = backgroundColor,
        contentColor = iconColor,
        tonalElevation = 0.dp,
        shadowElevation = shadowElevation
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
fun CircleButtonAdaptive(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String? = null,
    iconScale: Float = 0.62f,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = backgroundColor,
        contentColor = iconColor,
        tonalElevation = 0.dp,
        shadowElevation = shadowElevation
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.fillMaxSize(iconScale)
            )
        }
    }
}

@Composable
fun SettingsButtonAdaptive(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    textColor: Color = ConstColours.WHITE,
    iconColor: Color = textColor,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ConstColours.BLACK,
            )
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                color = textColor,
                style = AppTextStyles.MainText
            )
        }
    }
}

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    textColor: Color = ConstColours.WHITE,
    iconColor: Color = textColor,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ConstColours.BLACK,
            )
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                color = textColor,
                style = AppTextStyles.MainText
            )
        }
    }
}

@Composable
fun FriendsPillButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_friends),
    height: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    contentColor: Color = ConstColours.WHITE,
    elevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.6f),
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 0.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Group,
            contentDescription = null
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = ConstColours.WHITE
        )
    }
}

@Composable
fun FriendsPillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_friends),
    height: Dp = 50.dp,
    iconSize: Dp = 20.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    contentColor: Color = ConstColours.WHITE,
    elevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.6f),
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 0.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Group,
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = ConstColours.WHITE
        )
    }
}

@Composable
fun BigCircleForMainScreenActionAdaptive(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongPressStart: () -> Unit,
    onLongPressEnd: () -> Unit,
    onStartProgress: () -> Unit = {},
    onEndProgress: () -> Unit = {},
    ring: Dp = 14.dp,
    enabled: Boolean = true,
    progressStarted: MutableState<Boolean> = mutableStateOf(false)
) {
    var pressed by remember { mutableStateOf(false) }
    var longMode by remember { mutableStateOf(false) }

    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun startPulsations() {
        scope.launch {
            progress.snapTo(0f)
            progressStarted.value = true
            while (pressed) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 750,
                        easing = LinearEasing
                    )
                )
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 750,
                        easing = LinearEasing
                    )
                )
                if (!(progressStarted.value && pressed)) {
                    break
                }
            }

            progressStarted.value = false
            progress.snapTo(0f)
        }
    }

    fun stopPulsations(reset: Boolean = true) {
        scope.launch {
            progress.stop()
            if (reset) progress.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (progressStarted.value && pressed) ConstColours.MAIN_BRAND_BLUE_ALPHA40 else ConstColours.MAIN_BACK_GRAY),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(0.8f + 0.2f * progress.value)
                //.padding(ring - (ring.value * progress.value).dp)
                .clip(CircleShape)
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectTapGestures(
                        onTap = { onClick() },
                        onLongPress = {
                            longMode = true
                            onLongPressStart()
                            onStartProgress()
                            startPulsations()
                        },
                        onPress = {
                            pressed = true
                            progressStarted.value = true
                            val released = tryAwaitRelease()
                            pressed = false
                            if (longMode) {
                                onLongPressEnd()
                                longMode = false
                                onEndProgress()
                                stopPulsations()
                            }
                        }
                    )
                }
                .background(ConstColours.WHITE)
        )
    }
}

@Composable
fun BigCircleForMainScreenAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongPressStart: () -> Unit,
    onLongPressEnd: () -> Unit,
    onStartProgress: () -> Unit = {},
    onEndProgress: () -> Unit = {},
    size: Dp = 132.dp,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
    progressStarted: MutableState<Boolean> = mutableStateOf(false)
) {
    BigCircleForMainScreenActionAdaptive(
        modifier = modifier.size(size),
        onClick = onClick,
        onLongPressStart = onLongPressStart,
        onLongPressEnd = onLongPressEnd,
        onStartProgress = onStartProgress,
        onEndProgress = onEndProgress,
        ring = ring,
        enabled = enabled,
        progressStarted = progressStarted
    )
}


@Composable
fun BigCircleSendPhotoActionAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    outerColor: Color = ConstColours.MAIN_BACK_GRAY,
    innerColor: Color = ConstColours.WHITE,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .background(outerColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .clip(CircleShape)
                .background(innerColor)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Send,
            contentDescription = "Send",
            tint = ConstColours.BLACK,
            modifier = Modifier
                .fillMaxSize(0.34f)
                .rotate(-30f)
        )
    }
}

@Composable
fun BigCircleSendPhotoAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 132.dp,
    outerColor: Color = ConstColours.MAIN_BACK_GRAY,
    innerColor: Color = ConstColours.WHITE,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
) {
    BigCircleSendPhotoActionAdaptive(
        onClick = onClick,
        modifier = modifier.size(size),
        outerColor = outerColor,
        innerColor = innerColor,
        ring = ring,
        enabled = enabled
    )
}

@Composable
fun BigCircleMicroButtonAdaptive(
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onLongPressEnd: () -> Unit = {},
    onStartProgress: () -> Unit = {},
    onEndProgress: () -> Unit = {},
    modifier: Modifier = Modifier,
    outerColor: Color = ConstColours.MAIN_BACK_GRAY,
    innerColor: Color = ConstColours.WHITE,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
    isRecording: Boolean = false,
    progressStarted: MutableState<Boolean> = mutableStateOf(false)
) {
    var pressed by remember { mutableStateOf(false) }
    var longMode by remember { mutableStateOf(false) }

    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun startPulsations() {
        scope.launch {
            progress.snapTo(0f)
            progressStarted.value = true
            while (pressed) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 750,
                        easing = LinearEasing
                    )
                )
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 750,
                        easing = LinearEasing
                    )
                )
                if (!(progressStarted.value && pressed)) {
                    break
                }
            }
            progressStarted.value = false
            progress.snapTo(0f)
        }
    }

    fun stopPulsations(reset: Boolean = true) {
        scope.launch {
            progress.stop()
            if (reset) progress.snapTo(0f)
            progressStarted.value = false
        }
    }

    val backgroundColor = when {
        isRecording -> ConstColours.MAIN_BRAND_BLUE_ALPHA40
        else -> outerColor
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput

                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        longMode = true
                        onLongPress()
                        onStartProgress()
                        startPulsations()
                    },
                    onPress = {
                        pressed = true
                        progressStarted.value = true
                        val released = tryAwaitRelease()
                        pressed = false
                        if (longMode) {
                            onLongPressEnd()
                            longMode = false
                            onEndProgress()
                            stopPulsations()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f + 0.2f * progress.value)
                //.padding(ring - (ring.value * progress.value).dp)
                .clip(CircleShape)
                .background(innerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = "Mic",
                tint = ConstColours.BLACK,
                modifier = Modifier.fillMaxSize(0.34f)
            )
        }
    }
}

@Composable
fun BigCircleMicroButton(
    onClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onLongPressEnd: () -> Unit = {},
    onStartProgress: () -> Unit = {},
    onEndProgress: () -> Unit = {},
    modifier: Modifier = Modifier,
    size: Dp = 132.dp,
    outerColor: Color = ConstColours.MAIN_BACK_GRAY,
    innerColor: Color = ConstColours.WHITE,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
    isRecording: Boolean = false,
    progressStarted: MutableState<Boolean> = mutableStateOf(false)
) {
    BigCircleMicroButtonAdaptive(
        onClick = onClick,
        onLongPress = onLongPress,
        onLongPressEnd = onLongPressEnd,
        onStartProgress = onStartProgress,
        onEndProgress = onEndProgress,
        modifier = modifier.size(size),
        outerColor = outerColor,
        innerColor = innerColor,
        ring = ring,
        enabled = enabled,
        isRecording = isRecording,
        progressStarted = progressStarted
    )
}


@Composable
fun EditButtonAdaptive(
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.MAIN_BRAND_BLUE,
        contentColor = ConstColours.WHITE
    )
) {
    Button(
        onClick = onEditProfile,
        colors = colors,
        shape = RoundedCornerShape(50.dp),
        modifier = modifier.heightIn(min = 32.dp),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.small_padding),
            vertical = 0.dp
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = colors.contentColor
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.extra_small_padding)))
        Text(
            text = stringResource(R.string.button_edit),
            style = AppTextStyles.SubButtonText.copy(
                textAlign = TextAlign.Center,
                color = colors.contentColor
            )
        )
    }
}

@Composable
fun EditButton(
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 18.dp,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.MAIN_BRAND_BLUE,
        contentColor = ConstColours.WHITE
    )
) {
    Button(
        onClick = onEditProfile,
        colors = colors,
        shape = RoundedCornerShape(50.dp),
        modifier = modifier.heightIn(min = 32.dp),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.small_padding),
            vertical = 0.dp
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = colors.contentColor
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.extra_small_padding)))
        Text(
            text = stringResource(R.string.button_edit),
            style = AppTextStyles.SubButtonText.copy(
                textAlign = TextAlign.Center,
                color = colors.contentColor
            )
        )
    }
}

@Composable
fun PlusButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButtonAdaptive(
        onClick = onClick,
        icon = Icons.Filled.Add,
        contentDescription = stringResource(R.string.add_photo),
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun PlusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = ConstColours.MAIN_BACK_GRAY,
    iconColor: Color = ConstColours.WHITE,
    shadowElevation: Dp = 6.dp,
    enabled: Boolean = true,
) {
    CircleButton(
        onClick = onClick,
        icon = Icons.Filled.Add,
        contentDescription = stringResource(R.string.add_photo),
        size = size,
        iconSize = size * 0.62f,
        backgroundColor = backgroundColor,
        iconColor = iconColor,
        shadowElevation = shadowElevation,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun ContinueButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_continue),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.TRANSPARENT_WHITE_ALPHA0,
        contentColor = ConstColours.WHITE
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        ConstColours.MAIN_BRAND_BLUE,
                        ConstColours.MAIN_BRAND_BLUE.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(50.dp)

            ),
        shape = RoundedCornerShape(50.dp),
        colors = colors,
        contentPadding = PaddingValues()
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.ButtonText.copy(
                textAlign = TextAlign.Center,
                color = colors.contentColor
            )
        )
    }
}

@Composable
fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_continue),
    height: Dp = dimensionResource(R.dimen.button_size),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.TRANSPARENT_WHITE_ALPHA0,
        contentColor = ConstColours.WHITE
    )
) {
    ContinueButtonAdaptive(
        onClick = onClick,
        modifier = modifier.height(height),
        text = text,
        colors = colors
    )
}

@Composable
fun CancelButtonAdaptive(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_cancel),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.TRANSPARENT_WHITE_ALPHA0,
        contentColor = ConstColours.MAIN_BRAND_BLUE,
    )
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        colors = colors,
        border = BorderStroke(
            width = dimensionResource(R.dimen.thickness_divider),
            color = colors.contentColor
        )
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.ButtonText.copy(
                textAlign = TextAlign.Center,
                color = colors.contentColor
            ),
            textAlign = TextAlign.Center,
            color = colors.contentColor
        )
    }
}

@Composable
fun CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_cancel),
    height: Dp = dimensionResource(R.dimen.button_size),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.TRANSPARENT_WHITE_ALPHA0,
        contentColor = ConstColours.MAIN_BRAND_BLUE,
    )
) {
    CancelButtonAdaptive(
        onClick = onClick,
        modifier = modifier.height(height),
        text = text,
        colors = colors
    )
}

@Composable
fun BuyButtonAdaptive(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = stringResource(R.string.settings_premium_buy),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.GOLD,
        contentColor = ConstColours.WHITE
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        colors = colors
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.ButtonText.copy(
                textAlign = TextAlign.Center,
                color = colors.contentColor
            )
        )
    }
}

@Composable
fun BuyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = stringResource(R.string.settings_premium_buy),
    height: Dp = dimensionResource(R.dimen.button_size),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.GOLD,
        contentColor = ConstColours.WHITE
    )
) {
    BuyButtonAdaptive(
        modifier = modifier.height(height),
        onClick = onClick,
        text = text,
        colors = colors
    )
}

@Composable
fun StableSwitch(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 52.dp,
    height: Dp = 32.dp,
    thumbSize: Dp = 24.dp,
    enabledTrackColor: Color = ConstColours.MAIN_BRAND_BLUE,
    disabledTrackColor: Color = ConstColours.MAIN_BACK_GRAY,
    thumbColor: Color = ConstColours.WHITE
) {
    val trackRadius = height / 2
    val thumbRadius = thumbSize / 2

    val thumbOffset by animateDpAsState(
        targetValue = if (enabled) width - thumbSize - 6.dp else 6.dp,
        label = "thumbOffset"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(trackRadius))
            .background(if (enabled) enabledTrackColor else disabledTrackColor)
            .clickable { onEnabledChange(!enabled) }
            .padding(horizontal = 0.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}


@Composable
fun SwitchRow(
    text: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = AppTextStyles.MainText,
            color = ConstColours.WHITE,
            modifier = Modifier
                .weight(1f)
        )

        StableSwitch(
            enabled = enabled,
            onEnabledChange = onEnabledChange
        )
    }
}

@Composable
fun PremiumFeatureItem(
    text: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ConstColours.GOLD,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = AppTextStyles.MainText,
            color = ConstColours.WHITE
        )
    }
}

@Composable
fun SubscriptionOptionCard(
    option: SubscriptionOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                ConstColours.MAIN_BRAND_BLUE.copy(alpha = 0.2f)
            else ConstColours.MAIN_BACK_GRAY
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = ConstColours.MAIN_BRAND_BLUE,
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = option.title,
                    style = AppTextStyles.SubHeadlines.copy(fontSize = 14.sp),
                    color = if (isSelected) ConstColours.MAIN_BRAND_BLUE else ConstColours.WHITE
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = option.price,
                    style = AppTextStyles.Headlines.copy(fontSize = 16.sp),
                    color = ConstColours.WHITE
                )

                Text(
                    text = option.month_cost,
                    style = AppTextStyles.SupportingText.copy(fontSize = 12.sp),
                    color = ConstColours.SUPPORTING_TEXT
                )
            }
        }
    }
}

@Composable
fun ButtonForDeleteAdaptive(
    onClick: () -> Unit,
    text: String,
    color: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier,
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ConstColours.WHITE,
            contentColor = ConstColours.WHITE
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}

@Composable
fun ButtonForDelete(
    onClick: () -> Unit,
    text: String,
    color: Color,
    horizontalPadding: Dp = 22.dp
) {
    Button(
        onClick = onClick,
        modifier = Modifier,
        shape = RoundedCornerShape(999.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ConstColours.WHITE,
            contentColor = ConstColours.WHITE
        ),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}


@Preview(name = "Subscription Option Card", group = "Buttons")
@Composable
fun SubscriptionOptionCardPreview() {
    SubscriptionOptionCard(
        option = SubscriptionOption(
            title = "Год",
            price = "1200",
            month_cost = "100",
        ),
        isSelected = false,
        onSelect = {}
    )
}

@Preview(name = "Buy Buttons", group = "Buttons")
@Composable
fun BuyButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BuyButtonAdaptive(
            modifier = Modifier,
            {}
        )
        BuyButton(
            modifier = Modifier,
            onClick = {}
        )
    }
}

@Preview(name = "Continue And Cancel Buttons", group = "Buttons", showBackground = true)
@Composable
fun ContinueButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ContinueButtonAdaptive(
            {}
        )
        CancelButtonAdaptive(
            {}
        )
        ContinueButton(
            {}
        )
        CancelButton(
            {}
        )
    }
}


@Preview(
    name = "HardCoded Circle Buttons",
    group = "HardCoded Buttons",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewCircleButtonsHardCoded() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackCircleButton(onClick = {})
        ProfileCircleButton(onClick = {})
        SettingsCircleButton(onClick = {})
        AddFriendCircleButton(onClick = {})
        EditCircleButton(onClick = {})
        PlusButton(onClick = {})
    }
}

@Preview(
    name = "Adaptive Circle Buttons",
    group = "Adaptive Buttons",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewCircleButtonsAdaptive() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(50.dp))
        ProfileCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(50.dp))
        SettingsCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(50.dp))
        AddFriendCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(50.dp))
        EditCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(50.dp))
        PlusButtonAdaptive(onClick = {}, modifier = Modifier.size(50.dp))
    }
}


@Preview(
    name = "Adaptive Buttons Overview",
    group = "Adaptive Buttons",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewAdaptiveButtonsOverview() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(48.dp))
            ProfileCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(48.dp))
            SettingsCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(48.dp))
            AddFriendCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(48.dp))
            EditCircleButtonAdaptive(onClick = {}, modifier = Modifier.size(48.dp))
            PlusButtonAdaptive(onClick = {}, modifier = Modifier.size(48.dp))
        }

        FriendsPillButtonAdaptive(onClick = {})

        SettingsButtonAdaptive(
            onClick = {},
            icon = Icons.Filled.Settings,
            text = "Конфиденциальность",
            textColor = ConstColours.GOLD
        )

        ContinueButtonAdaptive(onClick = {})
        CancelButtonAdaptive(onClick = {})
        BuyButtonAdaptive(onClick = {})
        EditButtonAdaptive(onEditProfile = {})
        ButtonForDeleteAdaptive(onClick = {}, text = "Удалить", color = ConstColours.ERROR_RED)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewFriendsPill() {
    Row(
        Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FriendsPillButtonAdaptive(onClick = {})
        FriendsPillButton(onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewSettingsButton() {
    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingsButtonAdaptive(
            onClick = {},
            icon = Icons.Filled.Settings,
            text = "Конфиденциальность",
            textColor = ConstColours.GOLD
        )
        SettingsButton(
            onClick = {},
            icon = Icons.Filled.Settings,
            text = "Конфиденциальность",
            textColor = ConstColours.GOLD
        )
    }
}


@Preview(
    name = "Adaptive Big Circle Actions",
    group = "Adaptive Buttons",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun PreviewBigCircleAdaptive() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BigCircleForMainScreenActionAdaptive(
            onClick = {},
            onLongPressStart = {},
            onLongPressEnd = {},
            modifier = Modifier.size(132.dp)
        )
        BigCircleSendPhotoActionAdaptive(
            onClick = {},
            modifier = Modifier.size(132.dp)
        )
        BigCircleMicroButtonAdaptive(
            modifier = Modifier.size(132.dp)
        )
    }
}

@Preview(
    name = "HardCoded Big Circle Actions",
    group = "HardCoded Buttons",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun PreviewBigCircleHardCoded() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BigCircleForMainScreenAction(
            onClick = {},
            onLongPressStart = {},
            onLongPressEnd = {}
        )
        BigCircleSendPhotoAction(onClick = {})
        BigCircleMicroButton()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewEdit() {
    Row(
        Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditButtonAdaptive({})
        EditButton({})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewDeleteButtons() {
    Row(
        Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonForDeleteAdaptive(onClick = {}, text = "Удалить", color = ConstColours.MAIN_BRAND_BLUE)
        ButtonForDelete(
            onClick = {},
            text = "Удалить",
            color = ConstColours.MAIN_BRAND_BLUE
        )
    }
}
