package com.project.momentum.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class MomentumThemeVariant {
    Momentum,
    AndroidSettings
}

private val MomentumStaticColours = MomentumColourDefaults.Static

private val MomentumDarkColorScheme = darkColorScheme(
    primary = MomentumStaticColours.mainBrandBlue,
    onPrimary = MomentumStaticColours.white,
    primaryContainer = MomentumStaticColours.mainBrandBlueAlpha40,
    onPrimaryContainer = MomentumStaticColours.white,
    secondary = MomentumStaticColours.mainBackGray,
    onSecondary = MomentumStaticColours.white,
    secondaryContainer = MomentumStaticColours.mainGlassGrayAlpha62,
    onSecondaryContainer = MomentumStaticColours.white,
    tertiary = MomentumStaticColours.gold,
    onTertiary = MomentumStaticColours.black,
    error = MomentumStaticColours.errorRed,
    onError = MomentumStaticColours.white,
    background = MomentumStaticColours.black,
    onBackground = MomentumStaticColours.white,
    surface = MomentumStaticColours.black,
    onSurface = MomentumStaticColours.white,
    surfaceVariant = MomentumStaticColours.mainBackGray,
    onSurfaceVariant = MomentumStaticColours.supportingText,
    outline = MomentumStaticColours.supportingSubText,
    inverseSurface = MomentumStaticColours.white,
    inverseOnSurface = MomentumStaticColours.black,
    inversePrimary = MomentumStaticColours.mainBrandBlue,
    scrim = MomentumStaticColours.black
)

private val MomentumLightColorScheme = lightColorScheme(
    primary = MomentumStaticColours.mainBrandBlue,
    onPrimary = MomentumStaticColours.white,
    primaryContainer = MomentumStaticColours.mainBrandBlue.copy(alpha = 0.12f),
    onPrimaryContainer = MomentumStaticColours.mainBrandBlue,
    secondary = MomentumStaticColours.mainBackGray,
    onSecondary = MomentumStaticColours.white,
    secondaryContainer = MomentumStaticColours.accountLogoTint,
    onSecondaryContainer = MomentumStaticColours.black,
    tertiary = MomentumStaticColours.gold,
    onTertiary = MomentumStaticColours.black,
    error = MomentumStaticColours.errorRed,
    onError = MomentumStaticColours.white,
    background = MomentumStaticColours.white,
    onBackground = MomentumStaticColours.black,
    surface = MomentumStaticColours.white,
    onSurface = MomentumStaticColours.black,
    surfaceVariant = MomentumStaticColours.accountLogoTint,
    onSurfaceVariant = MomentumStaticColours.supportingSubText,
    outline = MomentumStaticColours.supportingText,
    inverseSurface = MomentumStaticColours.black,
    inverseOnSurface = MomentumStaticColours.white,
    inversePrimary = MomentumStaticColours.mainBrandBlue,
    scrim = MomentumStaticColours.black
)

private fun momentumColorScheme(darkTheme: Boolean): ColorScheme =
    if (darkTheme) MomentumDarkColorScheme else MomentumLightColorScheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun androidSettingsColorScheme(darkTheme: Boolean): ColorScheme {
    val context = LocalContext.current
    return if (darkTheme) {
        dynamicDarkColorScheme(context)
    } else {
        dynamicLightColorScheme(context)
    }
}

@Composable
private fun resolveMomentumColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    themeVariant: MomentumThemeVariant
): ColorScheme = when {
    (dynamicColor || themeVariant == MomentumThemeVariant.AndroidSettings) &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> androidSettingsColorScheme(darkTheme)

    else -> momentumColorScheme(darkTheme)
}

private fun ColorScheme.toConstColours(
    dynamicColor: Boolean,
    themeVariant: MomentumThemeVariant
): MomentumColours =
    if (
        (dynamicColor || themeVariant == MomentumThemeVariant.AndroidSettings) &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    ) {
        MomentumColourDefaults.fromColorScheme(this)
    } else {
        MomentumColourDefaults.Static
    }

@Composable
fun MomentumAndroidSettingsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MomentumTheme(
        darkTheme = darkTheme,
        themeVariant = MomentumThemeVariant.AndroidSettings,
        content = content
    )
}

@Composable
fun MomentumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    themeVariant: MomentumThemeVariant = MomentumThemeVariant.Momentum,
    content: @Composable () -> Unit
) {
    val colorScheme = resolveMomentumColorScheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        themeVariant = themeVariant
    )
    ConstColours.use(
        colorScheme.toConstColours(
            dynamicColor = dynamicColor,
            themeVariant = themeVariant
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
