package com.herrhythm.app.presentation.screen.predictions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.presentation.common.components.ConfidenceBadge
import com.herrhythm.app.presentation.theme.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: PredictionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Predictions") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.prediction?.let { pred ->
                    item {
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Prediction Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    ConfidenceBadge(level = pred.confidenceLevel)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text("Predicted cycle length: ${pred.predictedCycleLength} days", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    item {
                        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MenstrualColor.copy(alpha = 0.1f))) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Next Period", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MenstrualColor)
                                Text(pred.nextPeriodStart.format(formatter), style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    item {
                        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = PmsColor.copy(alpha = 0.1f))) {
                            Column(Modifier.padding(16.dp)) {
                                Text("PMS Window Start", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PmsColor)
                                Text(pred.pmsWindowStart.format(formatter), style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    item {
                        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = OvulationColor.copy(alpha = 0.1f))) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Ovulation", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = OvulationColor)
                                Text(pred.ovulationDate.format(formatter), style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    item {
                        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = FertileColor.copy(alpha = 0.1f))) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Fertile Window", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FertileColor)
                                Text("${pred.fertileWindowStart.format(formatter)} — ${pred.fertileWindowEnd.format(formatter)}", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                if (uiState.pastCycles.isNotEmpty()) {
                    item {
                        Text("Past Cycles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    }
                    items(uiState.pastCycles) { cycle ->
                        PastCycleCard(cycle, formatter)
                    }
                }
            }
        }
    }
}

@Composable
private fun PastCycleCard(cycle: Cycle, formatter: DateTimeFormatter) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = cycle.startDate.format(formatter),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                cycle.cycleLength?.let { Text("Cycle: ${it}d", style = MaterialTheme.typography.bodyMedium) }
                cycle.periodLength?.let { Text("Period: ${it}d", style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}
