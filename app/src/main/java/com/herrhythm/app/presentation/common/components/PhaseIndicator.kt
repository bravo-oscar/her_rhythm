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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (cycleDay > 0) "Day $cycleDay" else "—",
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
