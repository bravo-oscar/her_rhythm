package com.herrhythm.app.presentation.screen.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.StyledCard
import com.herrhythm.app.presentation.theme.Rose
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri -> uri?.let { viewModel.exportData(context, it) } }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let { viewModel.importData(context, it) } }

    var notificationPermissionGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> notificationPermissionGranted = granted }

    val settings = uiState.settings

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            // Appearance section
            StyledCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("\uD83C\uDFA8 Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Theme", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        val options = listOf("light" to "Light", "dark" to "Dark", "system" to "System")
                        options.forEachIndexed { index, (value, label) ->
                            SegmentedButton(
                                selected = settings.themeMode == value,
                                onClick = { viewModel.updateSettings(settings.copy(themeMode = value)) },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = Rose.copy(alpha = 0.2f),
                                    activeContentColor = Rose
                                )
                            ) {
                                Text(label)
                            }
                        }
                    }
                }
            }

            // Notifications section
            StyledCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("\uD83D\uDD14 Notifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    if (!notificationPermissionGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Rose)
                        ) {
                            Text("Enable Notifications")
                        }
                        Spacer(Modifier.height(8.dp))
                    }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Notify before period")
                        Switch(
                            checked = settings.notifyBeforePeriod,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(notifyBeforePeriod = it)) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Rose,
                                checkedThumbColor = Color.White
                            )
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Notify PMS onset")
                        Switch(
                            checked = settings.notifyPmsOnset,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(notifyPmsOnset = it)) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Rose,
                                checkedThumbColor = Color.White
                            )
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Daily log reminder")
                        Switch(
                            checked = settings.dailyReminderEnabled,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(dailyReminderEnabled = it)) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Rose,
                                checkedThumbColor = Color.White
                            )
                        )
                    }
                }
            }

            // Prediction settings
            StyledCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("\uD83D\uDD2E Prediction", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    Text("PMS Window: ${settings.pmsWindowDays} days")
                    Slider(
                        value = settings.pmsWindowDays.toFloat(),
                        onValueChange = { viewModel.updateSettings(settings.copy(pmsWindowDays = it.roundToInt())) },
                        valueRange = 3f..14f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            thumbColor = Rose,
                            activeTrackColor = Rose
                        )
                    )

                    Text("Default cycle length: ${settings.defaultCycleLength} days")
                    Slider(
                        value = settings.defaultCycleLength.toFloat(),
                        onValueChange = { viewModel.updateSettings(settings.copy(defaultCycleLength = it.roundToInt())) },
                        valueRange = 21f..45f,
                        steps = 23,
                        colors = SliderDefaults.colors(
                            thumbColor = Rose,
                            activeTrackColor = Rose
                        )
                    )

                    Text("Cycles used for prediction: ${settings.predictionCycleCount}")
                    Slider(
                        value = settings.predictionCycleCount.toFloat(),
                        onValueChange = { viewModel.updateSettings(settings.copy(predictionCycleCount = it.roundToInt())) },
                        valueRange = 3f..12f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = Rose,
                            activeTrackColor = Rose
                        )
                    )

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Show fertile window")
                        Switch(
                            checked = settings.fertileWindowEnabled,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(fertileWindowEnabled = it)) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Rose,
                                checkedThumbColor = Color.White
                            )
                        )
                    }
                }
            }

            // Data section
            StyledCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("\uD83D\uDCBE Data", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { exportLauncher.launch("her_rhythm_backup.json") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose)
                        ) { Text("Export") }
                        OutlinedButton(
                            onClick = { importLauncher.launch(arrayOf("application/json")) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose)
                        ) { Text("Import") }
                    }

                    uiState.exportSuccess?.let { success ->
                        Text(
                            if (success) "Export successful!" else "Export failed",
                            color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    uiState.importSuccess?.let { success ->
                        Text(
                            if (success) "Import successful!" else "Import failed",
                            color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Demo Data
            StyledCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("\uD83E\uDDEA Demo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Load sample data to test the app, or clear everything to start fresh.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.loadDemoData() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Rose)
                        ) { Text("Load Demo Data") }
                        OutlinedButton(
                            onClick = { viewModel.clearAllData() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) { Text("Clear All Data") }
                    }

                    uiState.demoDataMessage?.let { message ->
                        Spacer(Modifier.height(4.dp))
                        Text(
                            message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // About
            StyledCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("\uD83D\uDC97 About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Her Rhythm v1.0", style = MaterialTheme.typography.bodyMedium)
                    Text("Privacy-first cycle tracker", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text("All data stays on your device.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("No accounts. No internet. No tracking.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
