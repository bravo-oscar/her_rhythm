package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.herrhythm.app.domain.model.LifestyleFactor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LifestyleChipGroup(
    selected: List<LifestyleFactor>,
    onToggle: (LifestyleFactor) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Lifestyle",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LifestyleFactor.entries.forEach { factor ->
                FilterChip(
                    selected = factor in selected,
                    onClick = { onToggle(factor) },
                    label = { Text("${factor.emoji} ${factor.displayName}") }
                )
            }
        }
    }
}
