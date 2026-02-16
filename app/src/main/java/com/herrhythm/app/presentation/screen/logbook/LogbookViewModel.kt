package com.herrhythm.app.presentation.screen.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.usecase.cycle.DeleteCycleUseCase
import com.herrhythm.app.domain.usecase.cycle.GetAllCyclesUseCase
import com.herrhythm.app.domain.usecase.log.GetAllLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogbookUiState(
    val logs: List<DailyLog> = emptyList(),
    val cycles: List<Cycle> = emptyList(),
    val selectedTab: Int = 0,
    val cycleToDelete: Cycle? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val getAllLogsUseCase: GetAllLogsUseCase,
    private val getAllCyclesUseCase: GetAllCyclesUseCase,
    private val deleteCycleUseCase: DeleteCycleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogbookUiState())
    val uiState: StateFlow<LogbookUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getAllLogsUseCase(),
                getAllCyclesUseCase()
            ) { logs, cycles ->
                _uiState.value.copy(
                    logs = logs,
                    cycles = cycles,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }

    fun selectTab(tab: Int) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun requestDeleteCycle(cycle: Cycle) {
        _uiState.update { it.copy(cycleToDelete = cycle) }
    }

    fun confirmDeleteCycle() {
        val cycle = _uiState.value.cycleToDelete ?: return
        viewModelScope.launch {
            deleteCycleUseCase(cycle)
            _uiState.update { it.copy(cycleToDelete = null) }
        }
    }

    fun dismissDeleteCycle() {
        _uiState.update { it.copy(cycleToDelete = null) }
    }
}
