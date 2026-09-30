package com.sahilarious.hoardr.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HoardrColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,

    val primary: Color,
    val primaryContainer: Color,
    val onPrimary: Color,

    val secondary: Color,
    val secondaryContainer: Color,
    val onSecondary: Color,

    val accent: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,

    val outline: Color,
    val outlineVariant: Color,

    val error: Color,
    val success: Color,
)

val LocalHoardrColors = staticCompositionLocalOf<HoardrColors> {
    LightHoardrColors
}

val MaterialTheme.hoardrColors: HoardrColors
    @Composable
    @ReadOnlyComposable
    get() = LocalHoardrColors.current
