package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.herrhythm.app.domain.model.Symptom

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SymptomChipGroup(
    selected: List<Symptom>,
    onToggle: (Symptom) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Symptoms",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Symptom.entries.forEach { symptom ->
                FilterChip(
                    selected = symptom in selected,
                    onClick = { onToggle(symptom) },
                    label = { Text(symptom.displayName) }
                )
            }
        }
    }
}
