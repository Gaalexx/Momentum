package com.project.momentum.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours
import com.project.momentum.BackCircleButton
import com.project.momentum.ui.theme.AppTextStyles


@Composable
fun TopBarTemplate(
    @StringRes title: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ((PaddingValues) -> Unit)
) {
    val bg = ConstColours.BLACK

    Scaffold(
        modifier = modifier
            .background(bg)
            .padding(16.dp),
        backgroundColor = bg,
        topBar = {
            Box(
                modifier
                    .fillMaxWidth()
            ) {
                BackCircleButton(
                    onClick = onBackClick
                )
                Text (
                    text = stringResource(title),
                    color = ConstColours.WHITE,
                    textAlign = TextAlign.Center,
                    style = AppTextStyles.Headlines,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        content = content
    )
}