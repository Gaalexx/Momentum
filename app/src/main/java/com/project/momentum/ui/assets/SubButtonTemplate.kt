package com.project.momentum.ui.assets

import android.provider.SyncStateContract
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.Constraints
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.R
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun SubButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = ConstColours.WHITE,
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(text),
            modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding)),
            color = textColor,
            textAlign = TextAlign.Center,
            style = AppTextStyles.SubButtonText,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SubButtonPreview() {
    Box(
        modifier = Modifier
            .background(ConstColours.MAIN_BACK_GRAY)
    ) {
        SubButton(
            text = R.string.template_sub_button,
            onClick = {}
        )
    }
}