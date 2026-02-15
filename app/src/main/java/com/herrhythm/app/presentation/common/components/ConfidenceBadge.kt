package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.herrhythm.app.domain.model.ConfidenceLevel
import com.herrhythm.app.presentation.theme.*

@Composable
fun ConfidenceBadge(
    level: ConfidenceLevel,
    modifier: Modifier = Modifier
) {
    val color = when (level) {
        ConfidenceLevel.LOW -> ConfidenceLow
        ConfidenceLevel.MODERATE -> ConfidenceModerate
        ConfidenceLevel.GOOD -> ConfidenceGood
        ConfidenceLevel.HIGH -> ConfidenceHigh
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = "${level.displayName} confidence",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
