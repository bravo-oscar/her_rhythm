package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.herrhythm.app.domain.model.FlowIntensity
import com.herrhythm.app.presentation.theme.Rose

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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FlowIntensity.entries.forEach { intensity ->
                val isSelected = selected == intensity
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(intensity) },
                    label = { Text(intensity.displayName) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Rose.copy(alpha = 0.85f),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFFFF5F3),
                    ),
                    border = if (!isSelected) FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = false,
                        borderColor = Color(0xFFE8D0D8),
                        borderWidth = 1.dp
                    ) else null
                )
            }
        }
    }
}
