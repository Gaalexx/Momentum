package com.project.momentum.ui.custom.gradientpicker

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object GradientPicker {
    private val colors: List<Color> = listOf(
        Color.White,
        Color.Red,
        Color.Cyan,
        Color.Blue,
        Color.Magenta,
        Color.Yellow
    )

    fun pick(): Brush {
        val rnd = Random.nextInt(0, 2)
        val colors: List<Color> = colors.shuffled().take(2)
        return when (rnd) {
            0 -> Brush.linearGradient(colors = colors)
            1 -> Brush.horizontalGradient(colors = colors)
            2 -> Brush.verticalGradient(colors = colors)
            else -> Brush.linearGradient(colors = colors)
        }
    }

}