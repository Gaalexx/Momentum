package com.project.momentum


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BackCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 65.dp,
    backgroundColor: Color = Color(0xFF2B2B2B),
    iconColor: Color = Color.White,
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
    size: Dp = 65.dp,
    backgroundColor: Color = Color(0xFF2B2B2B),
    iconColor: Color = Color.White,
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
    size: Dp = 65.dp,
    backgroundColor: Color = Color(0xFF2B2B2B),
    iconColor: Color = Color.White,
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
fun FriendsPillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Друзья",
    height: Dp = 44.dp,
    backgroundColor: Color = Color(0xFF2B2B2B),
    contentColor: Color = Color.White,
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
    size: Dp = 132.dp,
    outerColor: Color = Color(0xFF2B2B2B),
    innerColor: Color = Color.White,
    ring: Dp = 14.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewCircleButtons() {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
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
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        FriendsPillButton(onClick = {})
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewBigCircle() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BigCircleForMainScreenAction()
    }
}