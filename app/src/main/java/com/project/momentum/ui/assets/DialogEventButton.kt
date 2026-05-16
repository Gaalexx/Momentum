package com.project.momentum.ui.assets

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.ui.theme.AppTextStyles
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
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(text),
            modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding)),
            color = textColor,
            textAlign = TextAlign.Center,
            style = AppTextStyles.SubButtonText,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun DialogEventButton(
    @StringRes text: Int,
    image: Painter,
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
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(text),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.medium_padding)),
            color = textColor,
            textAlign = TextAlign.Center,
            style = AppTextStyles.SubButtonText,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
fun DialogEventButtonPreview() {
    Column {
        DialogEventButton(
            text = R.string.template_sub_button,
            icon = Icons.Outlined.Delete,
            onClick = {}
        )
        DialogEventButton(
            text = R.string.template_sub_button,
            image = painterResource(R.drawable.vk_logo),
            onClick = {}
        )
    }
}