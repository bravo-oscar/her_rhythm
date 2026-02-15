package com.herrhythm.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.herrhythm.app.domain.model.CyclePhase

private val LightColorScheme = lightColorScheme(
    primary = Rose,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE4EA),       // Richer pink
    onPrimaryContainer = RoseDark,
    secondary = Lavender,
    secondaryContainer = Color(0xFFF2E6FC),     // Richer lavender
    tertiary = Teal,
    tertiaryContainer = TealLight,
    background = Color(0xFFFFFAF6),             // Warmer
    surface = Color(0xFFFFFDFB),                // Faint warm tint
    surfaceVariant = Color(0xFFFFF5F3),
    onBackground = Color(0xFF2D2025),           // Deeper for contrast
    onSurface = Color(0xFF2D2025),              // Deeper for contrast
    onSurfaceVariant = Color(0xFF6D5060),
    outline = Color(0xFFD8C0C8),
    outlineVariant = Color(0xFFE8D8DC),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF0A8B8),                // Boosted pink
    onPrimary = RoseDark,
    primaryContainer = Rose,
    onPrimaryContainer = Color.White,
    secondary = LavenderLight,
    secondaryContainer = Lavender,
    tertiary = TealLight,
    tertiaryContainer = Teal,
    background = Color(0xFF1A1517),
    surface = Color(0xFF221D1F),
    surfaceVariant = Color(0xFF2D2528),
    onBackground = Color(0xFFF5ECF0),           // Warmer text
    onSurface = Color(0xFFF5ECF0),              // Warmer text
    onSurfaceVariant = Color(0xFFD8C8D0),       // Warmer
    outline = Color(0xFF4A3840),
    outlineVariant = Color(0xFF3A2E34),
)

private val AppTypography = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
)

object HerRhythmGradients {
    val screenBackground: Brush
        get() = Brush.verticalGradient(
            colors = listOf(GradientPinkTop, GradientCreamBottom)
        )

    val peachBackground: Brush
        get() = Brush.verticalGradient(
            colors = listOf(GradientPeachTop, GradientCreamBottom)
        )

    val darkScreenBackground: Brush
        get() = Brush.verticalGradient(
            colors = listOf(DarkGradientTop, DarkGradientBottom)
        )

    fun phaseRingGradient(phase: CyclePhase): List<Color> {
        return when (phase) {
            CyclePhase.MENSTRUAL -> listOf(MenstrualGradientStart, MenstrualGradientEnd)
            CyclePhase.FOLLICULAR -> listOf(FollicularGradientStart, FollicularGradientEnd)
            CyclePhase.OVULATION -> listOf(OvulationGradientStart, OvulationGradientEnd)
            CyclePhase.LUTEAL -> listOf(LutealGradientStart, LutealGradientEnd)
            CyclePhase.UNKNOWN -> listOf(UnknownGradientStart, UnknownGradientEnd)
        }
    }
}

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
        typography = AppTypography,
        content = content
    )
}
