package com.project.momentum.ui.assets

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.ui.theme.ConstColours

@Composable
fun DialogEventButton(
    @StringRes text: Int,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = ConstColours.WHITE
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        SubButton(
            text = text,
            onClick = {},
            textColor = textColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
fun DialogEventButtonPreview() {
    DialogEventButton(
        text = R.string.template_sub_button,
        icon = Icons.Outlined.Delete,
        onClick = {}
    )
}