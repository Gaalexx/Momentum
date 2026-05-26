package com.project.momentum.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class MomentumColours(
    val black: Color,
    val white: Color,
    val mainBrandBlue: Color,
    val mainBackGray: Color,
    val errorRed: Color,
    val red: Color,
    val delete: Color,
    val mainBrandBlueAlpha40: Color,
    val mainGlassGrayAlpha62: Color,
    val transparentWhiteAlpha0: Color,
    val supportingText: Color,
    val supportingSubText: Color,
    val gold: Color,
    val goldAlpha30: Color,
    val accountLogoTint: Color
)

object MomentumColourDefaults {
    val Static = MomentumColours(
        black = Color(0xFF181818),
        white = Color(0xFFFFFFFF),
        mainBrandBlue = Color(0xFF3766FF),
        mainBackGray = Color(0xFF2B2B2B),
        errorRed = Color(0xFFB3261E),
        red = Color.Red,
        delete = Color(0xFFFF0000),
        mainBrandBlueAlpha40 = Color(0x663766FF),
        mainGlassGrayAlpha62 = Color(0x9E2B2B2B),
        transparentWhiteAlpha0 = Color(0x00FFFFFF),
        supportingText = Color(0xFF888888),
        supportingSubText = Color(0xFF666666),
        gold = Color(0xFFFFAE00),
        goldAlpha30 = Color(0x4CFFAE00),
        accountLogoTint = Color(0xFFEDEEF2).copy(alpha = 0.7f)
    )

    fun fromColorScheme(colorScheme: ColorScheme): MomentumColours =
        MomentumColours(
            black = colorScheme.background,
            white = colorScheme.onBackground,
            mainBrandBlue = colorScheme.primary,
            mainBackGray = colorScheme.surfaceVariant,
            errorRed = colorScheme.error,
            red = colorScheme.error,
            delete = colorScheme.error,
            mainBrandBlueAlpha40 = colorScheme.primary.copy(alpha = 0.4f),
            mainGlassGrayAlpha62 = colorScheme.surfaceVariant.copy(alpha = 0.62f),
            transparentWhiteAlpha0 = Color.Transparent,
            supportingText = colorScheme.onSurfaceVariant,
            supportingSubText = colorScheme.outline,
            gold = colorScheme.tertiary,
            goldAlpha30 = colorScheme.tertiary.copy(alpha = 0.3f),
            accountLogoTint = colorScheme.onSurface.copy(alpha = 0.7f)
        )
}

val LocalMomentumColours = staticCompositionLocalOf { MomentumColourDefaults.Static }

object ConstColours {
    val BLACK: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.black
    val WHITE: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.white
    val MAIN_BRAND_BLUE: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.mainBrandBlue
    val MAIN_BACK_GRAY: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.mainBackGray
    val ERROR_RED: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.errorRed
    val RED: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.red
    val DELETE: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.delete
    val MAIN_BRAND_BLUE_ALPHA40: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.mainBrandBlueAlpha40
    val MAIN_GLASS_GRAY_ALPHA62: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.mainGlassGrayAlpha62
    val TRANSPARENT_WHITE_ALPHA0: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.transparentWhiteAlpha0
    val SUPPORTING_TEXT: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.supportingText
    val SUPPORTING_SUB_TEXT: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.supportingSubText
    val GOLD: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.gold
    val GOLD_ALPHA30: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.goldAlpha30
    val ACCOUNT_LOGO_TINT: Color
        @Composable @ReadOnlyComposable get() = LocalMomentumColours.current.accountLogoTint
}
