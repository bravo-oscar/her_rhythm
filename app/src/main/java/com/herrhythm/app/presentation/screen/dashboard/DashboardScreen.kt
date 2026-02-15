package com.herrhythm.app.presentation.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.presentation.common.components.ConfidenceBadge
import com.herrhythm.app.presentation.common.components.CountdownWidget
import com.herrhythm.app.presentation.common.components.PhaseIndicator
import com.herrhythm.app.presentation.theme.*

@Composable
fun DashboardScreen(
    onNavigateToCycleEntry: () -> Unit,
    onNavigateToPredictions: () -> Unit,
    onNavigateToDailyLog: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadData() }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = onNavigateToDailyLog,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.Default.EditNote, "Log today")
                }
                FloatingActionButton(onClick = onNavigateToCycleEntry) {
                    Icon(Icons.Default.Add, "New cycle")
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (!uiState.hasCycles) {
            // Empty state - no cycles logged yet
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "Welcome to Her Rhythm",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Start by logging your first cycle. Tap the + button to record when your period started.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Your predictions will improve with each cycle you log. All data stays on your device.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Her Rhythm",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(24.dp))

                uiState.prediction?.let { prediction ->
                    PhaseIndicator(
                        phase = prediction.currentPhase,
                        cycleDay = prediction.currentCycleDay
                    )

                    Spacer(Modifier.height(8.dp))
                    ConfidenceBadge(level = prediction.confidenceLevel)

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CountdownWidget(
                            label = "Next Period",
                            targetDate = prediction.nextPeriodStart,
                            color = MenstrualColor,
                            modifier = Modifier.weight(1f)
                        )
                        CountdownWidget(
                            label = "PMS Window",
                            targetDate = prediction.pmsWindowStart,
                            color = PmsColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CountdownWidget(
                            label = "Ovulation",
                            targetDate = prediction.ovulationDate,
                            color = OvulationColor,
                            modifier = Modifier.weight(1f)
                        )
                        CountdownWidget(
                            label = "Fertile Window",
                            targetDate = prediction.fertileWindowStart,
                            color = FertileColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    OutlinedButton(onClick = onNavigateToPredictions) {
                        Icon(Icons.Default.Analytics, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("View Detailed Predictions")
                    }
                }
            }
        }
    }
}
