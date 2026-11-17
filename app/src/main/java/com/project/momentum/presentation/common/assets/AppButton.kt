package com.project.momentum.presentation.common.assets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.project.momentum.R
import com.project.momentum.presentation.common.theme.AppTextStyles
import com.project.momentum.presentation.common.theme.ConstColours

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = ConstColours.MAIN_BRAND_BLUE,
    contentColor: Color = ConstColours.WHITE,
    textStyle: TextStyle = AppTextStyles.ButtonText,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.control_corner_radius)),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = textStyle.copy(textAlign = TextAlign.Center),
        )
    }
}
