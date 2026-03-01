package com.project.momentum.ui.assets


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CaptionBasicLabel(
    text: String,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (text.isBlank()) placeholder else text,
            color = if (text.isBlank()) Color.White.copy(alpha = 0.7f) else Color.White,
            style = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.White),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

