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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.momentum.ConstColours
import com.project.momentum.ui.theme.AppTextStyles
import com.skydoves.landscapist.ImageOptions

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

