package com.project.momentum.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.project.momentum.R

val Sensation = FontFamily(
    Font(R.font.sansation_light, weight = FontWeight.Light)
)

val Sansita = FontFamily(
    Font(R.font.sansita_regular, weight = FontWeight.Normal),
    Font(R.font.sansita_bold, weight = FontWeight.Bold)
)

object AppTextStyles {
    // Main text (Sansation Light, 20)
    val MainText = TextStyle(
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp
    )

    // Headlines (Sansita Bold, 20)
    val Headlines = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    // SubHeadlines (Sansita Bold, 16)
    val SubHeadlines = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    // Input text (Sansita Regular, 20)
    val InputText = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    // Supporting text (Sansation Light, 14)
    val SupportingText = TextStyle(
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )

    // Settings (Sansation Light, 14)
    val Settings = TextStyle(
        fontFamily = Sensation,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )

    // Button_text (Title Medium, 16 / lineHeight 24 / letterSpacing 0.15)
    val ButtonText = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )

    // Sub_button_text (Title Small, 14 / lineHeight 20 / letterSpacing 0.1)
    val SubButtonText = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    // User_name (Title Large, 22 / lineHeight 28 / letterSpacing 0)
    val UserName = TextStyle(
        fontFamily = Sansita,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
}

val AppTypography = Typography(
    // Headings / Titles
    titleLarge = AppTextStyles.Headlines,
    titleMedium = AppTextStyles.SubHeadlines,

    // Body
    bodyLarge = AppTextStyles.MainText,
    bodyMedium = AppTextStyles.InputText,
    bodySmall = AppTextStyles.SupportingText,

    // Labels (обычно под кнопки)
    labelLarge = AppTextStyles.ButtonText,
    labelMedium = AppTextStyles.SubButtonText,

    // Для User_name
    headlineSmall = AppTextStyles.UserName
)
