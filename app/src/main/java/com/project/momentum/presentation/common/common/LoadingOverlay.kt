package com.project.momentum.presentation.common.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.momentum.presentation.common.theme.ConstColours

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConstColours.BLACK),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = ConstColours.WHITE,
            modifier = Modifier.size(50.dp)
        )
    }
}