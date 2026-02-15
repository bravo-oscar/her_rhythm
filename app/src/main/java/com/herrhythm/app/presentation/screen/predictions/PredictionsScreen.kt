package com.herrhythm.app.presentation.screen.predictions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.presentation.common.components.ConfidenceBadge
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.StyledCard
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
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Predictions") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        GradientBackground {
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
                            StyledCard(Modifier.fillMaxWidth()) {
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
                            PhasePredictionCard(
                                emoji = "\uD83C\uDF38",
                                title = "Next Period",
                                date = pred.nextPeriodStart.format(formatter),
                                color = MenstrualColor
                            )
                        }

                        item {
                            PhasePredictionCard(
                                emoji = "\uD83C\uDF19",
                                title = "PMS Window Start",
                                date = pred.pmsWindowStart.format(formatter),
                                color = PmsColor
                            )
                        }

                        item {
                            PhasePredictionCard(
                                emoji = "\u2728",
                                title = "Ovulation",
                                date = pred.ovulationDate.format(formatter),
                                color = OvulationColor
                            )
                        }

                        item {
                            PhasePredictionCard(
                                emoji = "\uD83C\uDF31",
                                title = "Fertile Window",
                                date = "${pred.fertileWindowStart.format(formatter)} \u2014 ${pred.fertileWindowEnd.format(formatter)}",
                                color = FertileColor
                            )
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
}

@Composable
private fun PhasePredictionCard(
    emoji: String,
    title: String,
    date: String,
    color: Color
) {
    StyledCard(
        modifier = Modifier.fillMaxWidth(),
        accentColor = color,
        containerColor = color.copy(alpha = 0.14f)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "$emoji $title",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(date, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun PastCycleCard(cycle: Cycle, formatter: DateTimeFormatter) {
    StyledCard(Modifier.fillMaxWidth()) {
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
