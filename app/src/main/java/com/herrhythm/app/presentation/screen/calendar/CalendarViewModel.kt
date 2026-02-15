package com.herrhythm.app.presentation.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.model.Prediction
import com.herrhythm.app.domain.usecase.cycle.GetAllCyclesUseCase
import com.herrhythm.app.domain.usecase.log.GetAllLogsUseCase
import com.herrhythm.app.domain.usecase.prediction.GetPredictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val cycles: List<Cycle> = emptyList(),
    val logs: List<DailyLog> = emptyList(),
    val prediction: Prediction? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getAllCyclesUseCase: GetAllCyclesUseCase,
    private val getAllLogsUseCase: GetAllLogsUseCase,
    private val getPredictionUseCase: GetPredictionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            getAllCyclesUseCase().combine(getAllLogsUseCase()) { cycles, logs ->
                Pair(cycles, logs)
            }.collect { (cycles, logs) ->
                val prediction = try { getPredictionUseCase() } catch (e: Exception) { null }
                _uiState.update {
                    it.copy(cycles = cycles, logs = logs, prediction = prediction, isLoading = false)
                }
            }
        }
    }

    fun changeMonth(yearMonth: YearMonth) {
        _uiState.update { it.copy(currentMonth = yearMonth) }
    }
}
