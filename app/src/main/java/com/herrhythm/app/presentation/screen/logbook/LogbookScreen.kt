package com.herrhythm.app.presentation.screen.logbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.StyledCard
import com.herrhythm.app.presentation.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LogbookScreen(
    onAddEntry: () -> Unit,
    onEditEntry: (LocalDate) -> Unit,
    viewModel: LogbookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEntry,
                containerColor = Rose,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, "Add log")
            }
        }
    ) { padding ->
        GradientBackground {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.logs.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(
                        "No daily logs yet.\nTap + to add your first entry.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.logs, key = { it.id }) { log ->
                        LogCard(log = log, onClick = { onEditEntry(log.date) })
                    }
                }
            }
        }
    }
}

@Composable
private fun LogCard(log: DailyLog, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
    StyledCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = log.date.format(formatter),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Mood badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Lavender.copy(alpha = 0.2f)
                ) {
                    Text(
                        "${log.mood.emoji} ${log.mood.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Lavender
                    )
                }
                // Flow badge
                if (log.flowIntensity.name != "NONE") {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Rose.copy(alpha = 0.2f)
                    ) {
                        Text(
                            "Flow: ${log.flowIntensity.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Rose
                        )
                    }
                }
            }
            if (log.symptoms.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = log.symptoms.joinToString(", ") { it.displayName },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (log.lifestyleFactors.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = log.lifestyleFactors.joinToString(", ") { "${it.emoji} ${it.displayName}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (log.note.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(Rose.copy(alpha = 0.4f))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = log.note,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
