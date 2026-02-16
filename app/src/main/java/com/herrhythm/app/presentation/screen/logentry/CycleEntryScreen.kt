package com.herrhythm.app.presentation.screen.logentry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.presentation.common.components.FlowIntensitySelector
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.GradientVariant
import com.herrhythm.app.presentation.common.components.StyledCard
import com.herrhythm.app.presentation.theme.Rose
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycleEntryScreen(
    cycleId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: CycleEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(cycleId) { viewModel.loadCycle(cycleId) }
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var startPickerKey by remember { mutableIntStateOf(0) }
    var endPickerKey by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Edit Cycle" else "New Cycle") },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start date
                StyledCard(
                    onClick = { startPickerKey++; showStartDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = Rose
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Rose,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text("Period Start Date", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(uiState.startDate.toString(), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                // End date (optional)
                StyledCard(
                    onClick = { endPickerKey++; showEndDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    accentColor = Rose
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Rose,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text("Period End Date (optional)", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                uiState.endDate?.toString() ?: "Tap to set",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (uiState.endDate == null) MaterialTheme.colorScheme.onSurfaceVariant
                                       else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                FlowIntensitySelector(
                    selected = uiState.flowIntensity,
                    onSelect = viewModel::updateFlowIntensity
                )

                OutlinedTextField(
                    value = uiState.notes,
                    onValueChange = viewModel::updateNotes,
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

                Button(
                    onClick = viewModel::save,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Rose),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(if (uiState.isEditing) "Update Cycle" else "Save Cycle")
                }
            }
        }
    }

    if (showStartDatePicker) {
        key(startPickerKey) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = uiState.startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showStartDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                            viewModel.updateStartDate(date)
                        }
                        showStartDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
                }
            ) { DatePicker(state = datePickerState) }
        }
    }

    if (showEndDatePicker) {
        key(endPickerKey) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = (uiState.endDate ?: uiState.startDate)
                    .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                            viewModel.updateEndDate(date)
                        }
                        showEndDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
                }
            ) { DatePicker(state = datePickerState) }
        }
    }
}
