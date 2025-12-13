package com.project.momentum.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography
import com.project.momentum.R



val Sensation = FontFamily(
    Font(R.font.sansation_light, weight = FontWeight.Light)
)

val Sansita = FontFamily(
    Font(R.font.sansita_regular, weight = FontWeight.Normal),
    Font(R.font.sansita_bold, weight = FontWeight.Bold)
)


object AppTextStyles {
    val MainText = TextStyle(
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp
    )

    val Headlines = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    val InputText = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    val SupportingText = TextStyle(
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )
}

val AppTypography = Typography(
    titleLarge = TextStyle( // Headlines
        fontFamily = Sansita,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle( // MainText
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle( // InputText
        fontFamily = Sansita,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodySmall = TextStyle( // SupportingText
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )
)
