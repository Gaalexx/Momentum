package com.project.momentum


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.rounded.AirplaneTicket
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.automirrored.rounded.SendToMobile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.momentum.ConstColours
import com.project.momentum.ui.theme.AppTextStyles
import com.skydoves.landscapist.ImageOptions
import androidx.compose.ui.graphics.ColorFilter

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
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 132.dp,
    outerColor: Color = ConstColours.MAIN_BACK_GRAY,
    innerColor: Color = ConstColours.WHITE,
    ring: Dp = 14.dp,
    enabled: Boolean = true,
    colorFilter: ColorFilter? = null,
    isRecording: Boolean = false
) {
    val actualInnerColor = if (isRecording) Color.Red else innerColor
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
                .background(actualInnerColor)
        )

        Icon(
            imageVector = Icons.Outlined.Mic,
            contentDescription = "Mic",
            tint = if (isRecording) Color.White else ConstColours.BLACK,
            modifier = Modifier
                .size(45.dp)
        )
    }
}


@Composable
fun EditButton(onEditProfile : () -> Unit) {
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
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        colors = colors
    ) {
        Text (
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.ButtonText.copy(
                textAlign = TextAlign.Center
            )
        )
    }
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
        BigCircleForMainScreenAction({})
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
