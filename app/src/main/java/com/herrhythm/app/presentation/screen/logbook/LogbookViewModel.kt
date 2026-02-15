package com.herrhythm.app.presentation.screen.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.usecase.log.GetAllLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogbookUiState(
    val logs: List<DailyLog> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val getAllLogsUseCase: GetAllLogsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogbookUiState())
    val uiState: StateFlow<LogbookUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllLogsUseCase().collect { logs ->
                _uiState.update { it.copy(logs = logs, isLoading = false) }
            }
        }
    }
}
