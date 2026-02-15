package com.herrhythm.app.presentation.screen.logentry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.presentation.common.components.FlowIntensitySelector
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.GradientDivider
import com.herrhythm.app.presentation.common.components.GradientVariant
import com.herrhythm.app.presentation.common.components.LifestyleChipGroup
import com.herrhythm.app.presentation.common.components.MoodChipGroup
import com.herrhythm.app.presentation.common.components.SymptomChipGroup
import com.herrhythm.app.presentation.theme.Rose

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
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Daily Log - ${uiState.date}") },
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
        GradientBackground(variant = GradientVariant.PEACH) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Section 1: Mood & Emotions
                Text(
                    text = "\uD83D\uDE0A Mood & Emotions",
                    style = MaterialTheme.typography.titleMedium
                )
                MoodChipGroup(
                    selected = uiState.mood,
                    onSelect = viewModel::updateMood
                )

                GradientDivider()

                // Section 2: Symptoms
                Text(
                    text = "\uD83E\uDE7A Symptoms",
                    style = MaterialTheme.typography.titleMedium
                )
                SymptomChipGroup(
                    selected = uiState.symptoms,
                    onToggle = viewModel::toggleSymptom
                )

                GradientDivider()

                // Section 3: Flow
                Text(
                    text = "\uD83C\uDF38 Flow",
                    style = MaterialTheme.typography.titleMedium
                )
                FlowIntensitySelector(
                    selected = uiState.flowIntensity,
                    onSelect = viewModel::updateFlowIntensity
                )

                GradientDivider()

                // Section 4: Lifestyle
                Text(
                    text = "\uD83C\uDF31 Lifestyle",
                    style = MaterialTheme.typography.titleMedium
                )
                LifestyleChipGroup(
                    selected = uiState.lifestyleFactors,
                    onToggle = viewModel::toggleLifestyleFactor
                )

                GradientDivider()

                // Section 5: Temperature + Notes
                OutlinedTextField(
                    value = uiState.temperature,
                    onValueChange = viewModel::updateTemperature,
                    label = { Text("Temperature (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Rose,
                        focusedLabelColor = Rose,
                        cursorColor = Rose
                    )
                )

                OutlinedTextField(
                    value = uiState.note,
                    onValueChange = viewModel::updateNote,
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Rose,
                        focusedLabelColor = Rose,
                        cursorColor = Rose
                    )
                )

                // Section 6: Save
                Button(
                    onClick = viewModel::save,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Rose),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(if (uiState.isEditing) "Update Log" else "Save Log")
                }
            }
        }
    }
}
