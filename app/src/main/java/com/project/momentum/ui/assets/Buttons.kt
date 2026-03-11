package com.project.momentum.ui.assets


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Mic
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
import com.example.momentum.ConstColours
import com.project.momentum.ui.theme.AppTextStyles
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import com.project.momentum.R
import com.project.momentum.ui.screens.settings.SubscriptionOption

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
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = iconColor,
                modifier = Modifier.size(size * 0.55f)
            )
        }
    }
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
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "Profile",
                tint = iconColor,
                modifier = Modifier.size(size * 0.62f)
            )
        }
    }
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
                contentDescription = "Settings",
                tint = iconColor,
                modifier = Modifier.size(size * 0.62f)
            )
        }
    }
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
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
                modifier = Modifier.size(size * 0.62f)
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
                modifier = Modifier.size(24.dp)
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
fun FriendsPillButton(
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
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
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
    var pressed by remember { mutableStateOf(false) }
    var longMode by remember { mutableStateOf(false) }

    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun startPulsations(){
        scope.launch {
            progress.snapTo(0f)
            progressStarted.value = true
            while(pressed){
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
                if(!(progressStarted.value && pressed)){
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
            .size(size)
            .clip(CircleShape)
            .background(if (progressStarted.value && pressed) ConstColours.MAIN_BRAND_BLUE_ALPHA40 else ConstColours.MAIN_BACK_GRAY),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ring - (ring.value * progress.value).dp)
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
fun BigCircleSendPhotoAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 132.dp,
    outerColor: Color = ConstColours.MAIN_BACK_GRAY,
    innerColor: Color = ConstColours.WHITE,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .background(outerColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ring)
                .clip(CircleShape)
                .background(innerColor)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Send,
            contentDescription = "Send",
            tint = ConstColours.BLACK,
            modifier = Modifier
                .size(45.dp)
                .rotate(-30f)
        )
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
    var pressed by remember { mutableStateOf(false) }
    var longMode by remember { mutableStateOf(false) }

    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun startPulsations() {
        scope.launch {
            progress.snapTo(0f)
            progressStarted.value = true
            while(pressed) {
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
                if(!(progressStarted.value && pressed)) {
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
            .size(size)
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
                .fillMaxSize()
                .padding(ring - (ring.value * progress.value).dp)
                .clip(CircleShape)
                .background(innerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = "Mic",
                tint = ConstColours.BLACK,
                modifier = Modifier.size(45.dp)
            )
        }
    }
}


@Composable
fun EditButton(onEditProfile: () -> Unit) {
    Button(
        onClick = onEditProfile,
        colors = ButtonDefaults.buttonColors(
            containerColor = ConstColours.MAIN_BRAND_BLUE,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Редактировать",
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("Редактировать")
    }
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
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_photo),
                tint = iconColor,
                modifier = Modifier.size(size * 0.62f)
            )
        }
    }
}

@Composable
fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_continue),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ConstColours.MAIN_BRAND_BLUE,
        contentColor = ConstColours.WHITE
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
            .height(dimensionResource(R.dimen.button_size)),
        shape = RoundedCornerShape(50.dp),
        colors = colors
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.ButtonText.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun BuyButton(
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
        modifier = modifier.fillMaxWidth()
            .height(dimensionResource(R.dimen.button_size)),
        shape = RoundedCornerShape(50.dp),
        colors = colors
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.ButtonText.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun StableSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 52.dp,
    height: Dp = 32.dp,
    thumbSize: Dp = 24.dp,
    checkedTrackColor: Color = ConstColours.MAIN_BRAND_BLUE,
    uncheckedTrackColor: Color = ConstColours.MAIN_BACK_GRAY,
    thumbColor: Color = ConstColours.WHITE
) {
    val trackRadius = height / 2
    val thumbRadius = thumbSize / 2

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - 6.dp else 6.dp,
        label = "thumbOffset"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(trackRadius))
            .background(if (checked) checkedTrackColor else uncheckedTrackColor)
            .clickable { onCheckedChange(!checked) }
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
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun PremiumFeatureItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
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

@Preview
@Composable
fun BuyButtonPreview() {
    BuyButton(
        modifier = Modifier,
        {}
    )
}

@Preview
@Composable
fun ContinueButtonPreview() {
    ContinueButton(
        {}
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewCircleButtons() {
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
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewFriendsPill() {
    Box(contentAlignment = Alignment.Center) {
        FriendsPillButton(onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewSettingsButton() {
    Box(contentAlignment = Alignment.Center) {
        SettingsButton(
            onClick = {},
            icon = Icons.Filled.Settings,
            text = "Конфиденциальность",
            textColor = ConstColours.GOLD
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewBigCircle() {
    Box(contentAlignment = Alignment.Center) {
        BigCircleForMainScreenAction(onClick = {}, onLongPressStart = {}, onLongPressEnd = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewBigCircleForPhotoSend() {
    Box(contentAlignment = Alignment.Center) {
        BigCircleSendPhotoAction({})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewEdit() {
    Box(contentAlignment = Alignment.Center) {
        EditButton({})
    }
}
