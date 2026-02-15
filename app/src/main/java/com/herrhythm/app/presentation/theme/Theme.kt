package com.herrhythm.app.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Rose,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = RoseLight,
    onPrimaryContainer = RoseDark,
    secondary = Lavender,
    secondaryContainer = LavenderLight,
    tertiary = Teal,
    tertiaryContainer = TealLight,
    background = androidx.compose.ui.graphics.Color(0xFFFFFBFE),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFBFE),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
)

private val DarkColorScheme = darkColorScheme(
    primary = RoseLight,
    onPrimary = RoseDark,
    primaryContainer = Rose,
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,
    secondary = LavenderLight,
    secondaryContainer = Lavender,
    tertiary = TealLight,
    tertiaryContainer = Teal,
)

@Composable
fun HerRhythmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
