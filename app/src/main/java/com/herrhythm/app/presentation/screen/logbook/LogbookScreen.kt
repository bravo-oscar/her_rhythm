package com.herrhythm.app.presentation.screen.logbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.Cycle
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
    onEditCycle: (Long) -> Unit,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Rose,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(color = Rose)
                    }
                ) {
                    Tab(
                        selected = uiState.selectedTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        text = { Text("Daily Logs") },
                        selectedContentColor = Rose,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Tab(
                        selected = uiState.selectedTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        text = { Text("Cycles") },
                        selectedContentColor = Rose,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (uiState.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.selectedTab == 0) {
                    if (uiState.logs.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "No daily logs yet.\nTap + to add your first entry.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.logs, key = { it.id }) { log ->
                                LogCard(log = log, onClick = { onEditEntry(log.date) })
                            }
                        }
                    }
                } else {
                    if (uiState.cycles.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "No cycles logged yet.\nLog a cycle from the Dashboard.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.cycles, key = { it.id }) { cycle ->
                                CycleCard(
                                    cycle = cycle,
                                    onClick = { onEditCycle(cycle.id) },
                                    onDelete = { viewModel.requestDeleteCycle(cycle) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    uiState.cycleToDelete?.let { cycle ->
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
        AlertDialog(
            onDismissRequest = viewModel::dismissDeleteCycle,
            title = { Text("Delete Cycle") },
            text = {
                Text("Delete the cycle starting ${cycle.startDate.format(formatter)}? This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::confirmDeleteCycle,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissDeleteCycle) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CycleCard(cycle: Cycle, onClick: () -> Unit, onDelete: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    StyledCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        accentColor = MenstrualColor
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cycle.startDate.format(formatter) +
                            (cycle.endDate?.let { " – ${it.format(formatter)}" } ?: ""),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete cycle",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Flow intensity badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Rose.copy(alpha = 0.2f)
                ) {
                    Text(
                        "Flow: ${cycle.flowIntensity.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Rose
                    )
                }
                // Period length badge
                cycle.periodLength?.let { length ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Lavender.copy(alpha = 0.2f)
                    ) {
                        Text(
                            "$length day${if (length != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Lavender
                        )
                    }
                }
            }
            if (cycle.notes.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(MenstrualColor.copy(alpha = 0.4f))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = cycle.notes,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
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
