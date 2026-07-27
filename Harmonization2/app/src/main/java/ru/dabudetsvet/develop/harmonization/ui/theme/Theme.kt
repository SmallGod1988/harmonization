package ru.dabudetsvet.develop.harmonization.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = HarmonyGreen,
    onPrimary = Color.White,
    secondary = HarmonyAmber,
    tertiary = HarmonyPurple,
    background = HarmonyBackgroundLight,
    surface = HarmonySurfaceLight,
)

private val DarkColors = darkColorScheme(
    primary = HarmonyGreenDark,
    secondary = HarmonyAmber,
    tertiary = HarmonyPurple,
    background = HarmonyBackgroundDark,
    surface = HarmonySurfaceDark,
)

@Composable
fun HarmonizationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HarmonizationTypography,
        content = content
    )
}
