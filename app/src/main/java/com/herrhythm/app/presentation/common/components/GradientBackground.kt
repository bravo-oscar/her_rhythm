package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import com.herrhythm.app.presentation.theme.HerRhythmGradients

enum class GradientVariant {
    DEFAULT,
    PEACH
}

@Composable
fun GradientBackground(
    variant: GradientVariant = GradientVariant.DEFAULT,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Use the actual theme's background color luminance to detect dark mode
    // This respects the app's themeMode setting, not just the system setting
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val gradient = if (isDark) {
        HerRhythmGradients.darkScreenBackground
    } else {
        when (variant) {
            GradientVariant.DEFAULT -> HerRhythmGradients.screenBackground
            GradientVariant.PEACH -> HerRhythmGradients.peachBackground
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        content()
    }
}
