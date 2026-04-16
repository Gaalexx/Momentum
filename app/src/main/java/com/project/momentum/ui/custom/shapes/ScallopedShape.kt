package com.project.momentum.ui.custom.shapes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ScallopedShape(
    private val scallopCount: Int = 16,
    private val scallopDepthRatio: Float = 0.12f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()

        val width = size.width
        val height = size.height
        val radius = min(width, height) / 2f
        val cx = width / 2f
        val cy = height / 2f

        val steps = 360
        val depth = radius * scallopDepthRatio

        for (i in 0..steps) {
            val t = i / steps.toFloat()
            val angle = (2.0 * PI * t).toFloat()

            val wave = ((1f - cos(scallopCount * angle)) / 2f)
            val r = radius - depth * wave

            val x = cx + r * cos(angle)
            val y = cy + r * sin(angle)

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        path.close()
        return Outline.Generic(path)
    }
}