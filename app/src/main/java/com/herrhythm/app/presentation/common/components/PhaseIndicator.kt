package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herrhythm.app.domain.model.CyclePhase
import com.herrhythm.app.presentation.theme.*

@Composable
fun PhaseIndicator(
    phase: CyclePhase,
    cycleDay: Int,
    modifier: Modifier = Modifier
) {
    val color = when (phase) {
        CyclePhase.MENSTRUAL -> MenstrualColor
        CyclePhase.FOLLICULAR -> FollicularColor
        CyclePhase.OVULATION -> OvulationColor
        CyclePhase.LUTEAL -> LutealColor
        CyclePhase.UNKNOWN -> MaterialTheme.colorScheme.outline
    }

    val emoji = when (phase) {
        CyclePhase.MENSTRUAL -> "\uD83C\uDF38"  // Cherry blossom
        CyclePhase.FOLLICULAR -> "\uD83C\uDF31" // Seedling
        CyclePhase.OVULATION -> "\u2728"         // Sparkles
        CyclePhase.LUTEAL -> "\uD83C\uDF19"     // Crescent moon
        CyclePhase.UNKNOWN -> "\uD83E\uDEB7"    // Lotus
    }

    val gradientColors = HerRhythmGradients.phaseRingGradient(phase)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(160.dp)
                .shadow(12.dp, CircleShape, ambientColor = color.copy(alpha = 0.3f), spotColor = color.copy(alpha = 0.2f))
                .clip(CircleShape)
                .background(color.copy(alpha = 0.08f))
                .drawBehind {
                    val strokeWidth = 6.dp.toPx()
                    val radius = (size.minDimension - strokeWidth) / 2f
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = gradientColors + gradientColors.first().let { listOf(it) },
                            center = Offset(size.width / 2f, size.height / 2f)
                        ),
                        radius = radius,
                        center = Offset(size.width / 2f, size.height / 2f),
                        style = Stroke(width = strokeWidth)
                    )
                }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = emoji,
                    fontSize = 28.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (cycleDay > 0) "Day $cycleDay" else "\u2014",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = phase.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = color,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
