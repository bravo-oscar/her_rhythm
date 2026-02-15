package com.herrhythm.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.herrhythm.app.domain.model.LifestyleFactor
import com.herrhythm.app.presentation.theme.Teal

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
                val isSelected = factor in selected
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggle(factor) },
                    label = { Text("${factor.emoji} ${factor.displayName}") },
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Teal.copy(alpha = 0.85f),
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
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
