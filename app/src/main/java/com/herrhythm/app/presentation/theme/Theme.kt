package com.herrhythm.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Rose,
    onPrimary = Color.White,
    primaryContainer = RoseLight,
    onPrimaryContainer = RoseDark,
    secondary = Lavender,
    secondaryContainer = LavenderLight,
    tertiary = Teal,
    tertiaryContainer = TealLight,
    background = Color(0xFFFFFBF7),          // Warm bright cream
    surface = Color(0xFFFFFFFF),             // Pure white for cards
    surfaceVariant = Color(0xFFFFF5F3),      // Light pink tint
    onBackground = Color(0xFF3D3035),        // Soft dark
    onSurface = Color(0xFF3D3035),           // Soft dark
    onSurfaceVariant = Color(0xFF6D5060),    // Muted mauve
)

private val DarkColorScheme = darkColorScheme(
    primary = RoseLight,
    onPrimary = RoseDark,
    primaryContainer = Rose,
    onPrimaryContainer = Color.White,
    secondary = LavenderLight,
    secondaryContainer = Lavender,
    tertiary = TealLight,
    tertiaryContainer = Teal,
    background = Color(0xFF1A1517),          // Dark warm
    surface = Color(0xFF221D1F),             // Dark surface
    surfaceVariant = Color(0xFF2D2528),      // Dark variant
    onBackground = Color(0xFFF0E8EC),        // Light pink-white
    onSurface = Color(0xFFF0E8EC),           // Light pink-white
    onSurfaceVariant = Color(0xFFD0C0C8),    // Muted pink-grey
)

@Composable
fun HerRhythmTheme(
    themeMode: String = "system",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
