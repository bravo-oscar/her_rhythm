package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.herrhythm.app.domain.model.FlowIntensity

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FlowIntensitySelector(
    selected: FlowIntensity,
    onSelect: (FlowIntensity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Flow Intensity",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FlowIntensity.entries.forEach { intensity ->
                FilterChip(
                    selected = selected == intensity,
                    onClick = { onSelect(intensity) },
                    label = { Text(intensity.displayName) }
                )
            }
        }
    }
}
