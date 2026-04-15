package com.project.momentum.ui.assets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.sqrt

@Composable
fun AudioRadialVisualizer(
    level: Float,
    modifier: Modifier = Modifier,
) {
    val normalizedLevel = level.coerceIn(0f, 1f)
    val animatedLevel by animateFloatAsState(
        targetValue = normalizedLevel,
        animationSpec = tween(80),
        label = "audio-visualizer-level",
    )

    val infinite = rememberInfiniteTransition(label = "audio-visualizer-motion")
    val wave by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "audio-visualizer-wave",
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val boosted = sqrt(animatedLevel.coerceIn(0f, 1f))
        val reactiveWave = wave * 0.4f * (0.3f + animatedLevel)
        val motion = boosted * 1.3f + reactiveWave

        val maxRadius = size.maxDimension
        val shift = size.minDimension * 0.5f * motion
        val dynamicCenter = Offset(
            x = center.x + shift,
            y = center.y - shift * 0.6f,
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF3A86FF).copy(alpha = 0.5f + animatedLevel * 0.45f),
                    Color(0xFFFF006E).copy(alpha = 0.3f + animatedLevel * 0.5f),
                    Color.Transparent,
                ),
                center = dynamicCenter,
                radius = maxRadius,
            ),
        )

        val radius = maxRadius * (0.25f + motion * 0.6f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFF006E),
                    Color(0xFF3A86FF),
                    Color.Transparent,
                ),
                radius = radius,
            ),
            radius = radius,
            center = center,
        )
    }
}
