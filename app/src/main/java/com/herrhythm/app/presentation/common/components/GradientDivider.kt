package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.herrhythm.app.presentation.theme.Lavender
import com.herrhythm.app.presentation.theme.Rose

@Composable
fun GradientDivider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Rose.copy(alpha = 0.2f),
                        Lavender.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
    )
}
