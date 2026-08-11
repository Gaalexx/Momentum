package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.project.momentum.ui.theme.ConstColours

@Composable
fun LockBubble(
    modifier: Modifier = Modifier,
    isLocked: Boolean
) {
    Box(
        modifier = modifier
            .clip(
                CircleShape
            )
            .background(color = ConstColours.MAIN_BACK_GRAY)
    ) {
        Icon(
            imageVector = if (!isLocked) Icons.Outlined.Lock else Icons.Outlined.Done,
            contentDescription = "Lock",
            tint = ConstColours.WHITE,
        )
    }
}