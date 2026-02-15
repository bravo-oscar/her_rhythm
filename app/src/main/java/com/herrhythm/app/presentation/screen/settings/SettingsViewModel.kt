package com.herrhythm.app.presentation.screen.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.data.local.DataSeeder
import com.herrhythm.app.domain.model.UserSettings
import com.herrhythm.app.domain.usecase.settings.GetSettingsUseCase
import com.herrhythm.app.domain.usecase.settings.UpdateSettingsUseCase
import com.herrhythm.app.domain.usecase.backup.ExportDataUseCase
import com.herrhythm.app.domain.usecase.backup.ImportDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: UserSettings = UserSettings(),
    val isLoading: Boolean = true,
    val exportSuccess: Boolean? = null,
    val importSuccess: Boolean? = null,
    val demoDataMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val exportDataUseCase: ExportDataUseCase,
    private val importDataUseCase: ImportDataUseCase,
    private val dataSeeder: DataSeeder
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getSettingsUseCase().collect { settings ->
                _uiState.update { it.copy(settings = settings, isLoading = false) }
            }
        }
    }

    fun updateSettings(settings: UserSettings) {
        viewModelScope.launch {
            updateSettingsUseCase(settings)
        }
    }

    fun exportData(context: Context, uri: android.net.Uri) {
        viewModelScope.launch {
            try {
                exportDataUseCase(context, uri)
                _uiState.update { it.copy(exportSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(exportSuccess = false) }
            }
        }
    }

    fun importData(context: Context, uri: android.net.Uri) {
        viewModelScope.launch {
            try {
                importDataUseCase(context, uri)
                _uiState.update { it.copy(importSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(importSuccess = false) }
            }
        }
    }

    fun clearExportImportStatus() {
        _uiState.update { it.copy(exportSuccess = null, importSuccess = null) }
    }

    fun loadDemoData() {
        viewModelScope.launch {
            try {
                dataSeeder.seedDemoData()
                _uiState.update { it.copy(demoDataMessage = "Demo data loaded! Go back to Dashboard to see it.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(demoDataMessage = "Failed to load demo data.") }
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                dataSeeder.clearAllData()
                _uiState.update { it.copy(demoDataMessage = "All data cleared.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(demoDataMessage = "Failed to clear data.") }
            }
        }
    }

    fun clearDemoDataMessage() {
        _uiState.update { it.copy(demoDataMessage = null) }
    }
}
