package com.herrhythm.app.presentation.screen.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.DailyLog
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
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEntry) {
                Icon(Icons.Default.Add, "Add log")
            }
        }
    ) { padding ->
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

@Composable
private fun LogCard(log: DailyLog, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = log.date.format(formatter),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${log.mood.emoji} ${log.mood.displayName}", style = MaterialTheme.typography.bodyMedium)
                if (log.flowIntensity.name != "NONE") {
                    Text("Flow: ${log.flowIntensity.displayName}", style = MaterialTheme.typography.bodyMedium)
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
                Spacer(Modifier.height(4.dp))
                Text(
                    text = log.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}
