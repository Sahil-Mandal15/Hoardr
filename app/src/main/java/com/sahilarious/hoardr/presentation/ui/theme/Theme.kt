package com.sahilarious.hoardr.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.sahilarious.hoardr.R

private val DarkHoardrColors = HoardrColors(
    background = Color(0xFF100D12),
    surface = Color(0xFF19151C),
    surfaceVariant = Color(0xFF241D28),
    primary = Color(0xFFFF6975),
    primaryContainer = Color(0xFF61252D),
    onPrimary = Color(0xFF3D0710),
    secondary = Color(0xFF947DFF),
    secondaryContainer = Color(0xFF342B59),
    onSecondary = Color(0xFF180F3D),
    accent = Color(0xFFFFA47F),
    textPrimary = Color(0xFFFFF7F5),
    textSecondary = Color(0xFFBEB3B8),
    textTertiary = Color(0xFF82767D),
    outline = Color(0xFF40363F),
    outlineVariant = Color(0xFF2C252D),
    error = Color(0xFFFFB4AB),
    success = Color(0xFF6DDAA0)
)

internal val LightHoardrColors = HoardrColors(
    background = Color(0xFFFFF9F7),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFFFF1EE),
    primary = Color(0xFFE74757),
    primaryContainer = Color(0xFFFFDADF),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF6448D8),
    secondaryContainer = Color(0xFFE9E3FF),
    onSecondary = Color(0xFFFFFFFF),
    accent = Color(0xFFFF9B72),
    textPrimary = Color(0xFF201A1C),
    textSecondary = Color(0xFF756B70),
    textTertiary = Color(0xFFA59A9F),
    outline = Color(0xFFE5D9DC),
    outlineVariant = Color(0xFFF0E8EA),
    error = Color(0xFFBA1A1A),
    success = Color(0xFF278A5B)
)

private val DarkColorScheme = darkColorScheme(
    background = DarkHoardrColors.background,
    surface = DarkHoardrColors.surface,
    surfaceVariant = DarkHoardrColors.surfaceVariant,
    primary = DarkHoardrColors.primary,
    primaryContainer = DarkHoardrColors.primaryContainer,
    onPrimary = DarkHoardrColors.onPrimary,
    secondary = DarkHoardrColors.secondary,
    secondaryContainer = DarkHoardrColors.secondaryContainer,
    onSecondary = DarkHoardrColors.onSecondary,
    outline = DarkHoardrColors.outline,
    outlineVariant = DarkHoardrColors.outlineVariant,
    error = DarkHoardrColors.error,
    onBackground = DarkHoardrColors.textPrimary,
    onSurface = DarkHoardrColors.textPrimary,
    onSurfaceVariant = DarkHoardrColors.textSecondary,
)

private val LightColorScheme = lightColorScheme(
    background = LightHoardrColors.background,
    surface = LightHoardrColors.surface,
    surfaceVariant = LightHoardrColors.surfaceVariant,
    primary = LightHoardrColors.primary,
    primaryContainer = LightHoardrColors.primaryContainer,
    onPrimary = LightHoardrColors.onPrimary,
    secondary = LightHoardrColors.secondary,
    secondaryContainer = LightHoardrColors.secondaryContainer,
    onSecondary = LightHoardrColors.onSecondary,
    outline = LightHoardrColors.outline,
    outlineVariant = LightHoardrColors.outlineVariant,
    error = LightHoardrColors.error,
    onBackground = LightHoardrColors.textPrimary,
    onSurface = LightHoardrColors.textPrimary,
    onSurfaceVariant = LightHoardrColors.textSecondary,
)

val Caveat = FontFamily(
    Font(R.font.caveat_regular, FontWeight.Normal),
    Font(R.font.caveat_bold, FontWeight.Bold)
)

val Quicksand = FontFamily(
    Font(R.font.quicksand_regular, FontWeight.Normal),
    Font(R.font.quicksand_semibold, FontWeight.SemiBold)
)

@Composable
fun HoardrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val hoardrColors = if (darkTheme) DarkHoardrColors else LightHoardrColors

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalHoardrColors provides hoardrColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
