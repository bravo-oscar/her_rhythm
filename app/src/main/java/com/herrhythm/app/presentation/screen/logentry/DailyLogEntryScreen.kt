package com.herrhythm.app.presentation.screen.logentry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.presentation.common.components.FlowIntensitySelector
import com.herrhythm.app.presentation.common.components.MoodChipGroup
import com.herrhythm.app.presentation.common.components.SymptomChipGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyLogEntryScreen(
    dateString: String,
    onNavigateBack: () -> Unit,
    viewModel: DailyLogEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(dateString) { viewModel.loadForDate(dateString) }
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Log - ${uiState.date}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MoodChipGroup(
                selected = uiState.mood,
                onSelect = viewModel::updateMood
            )

            SymptomChipGroup(
                selected = uiState.symptoms,
                onToggle = viewModel::toggleSymptom
            )

            FlowIntensitySelector(
                selected = uiState.flowIntensity,
                onSelect = viewModel::updateFlowIntensity
            )

            OutlinedTextField(
                value = uiState.temperature,
                onValueChange = viewModel::updateTemperature,
                label = { Text("Temperature (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::updateNote,
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isEditing) "Update Log" else "Save Log")
            }
        }
    }
}
