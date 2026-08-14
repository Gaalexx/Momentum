package com.project.momentum.features.contentcreation.ui.assets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.project.momentum.R
import com.project.momentum.ui.theme.ConstColours

@Composable
fun LockBubble(
    modifier: Modifier = Modifier, isLocked: Boolean, progress: () -> Float
) {

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = ConstColours.WHITE.toArgb(),
            keyPath = arrayOf("**")
        ), rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = ConstColours.WHITE.toArgb(),
            keyPath = arrayOf("**")
        )
    )

    Box(
        modifier = modifier
            .clip(
                CircleShape
            )
            .background(color = ConstColours.MAIN_BACK_GRAY), contentAlignment = Alignment.Center
    ) {

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lock)
        )
        LottieAnimation(
            composition = composition,
            progress = progress,
            dynamicProperties = dynamicProperties,
        )
    }
}


@Preview
@Composable
private fun LockBubblePreview() {
    LockBubble(modifier = Modifier.size(100.dp), false, { 0.25f })
}