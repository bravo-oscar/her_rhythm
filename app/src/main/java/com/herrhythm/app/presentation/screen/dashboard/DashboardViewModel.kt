package com.herrhythm.app.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.CycleStatistics
import com.herrhythm.app.domain.model.Prediction
import com.herrhythm.app.domain.usecase.cycle.GetAllCyclesUseCase
import com.herrhythm.app.domain.usecase.cycle.GetCurrentCycleUseCase
import com.herrhythm.app.domain.usecase.prediction.GetPredictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class DashboardUiState(
    val prediction: Prediction? = null,
    val currentCycle: Cycle? = null,
    val hasCycles: Boolean = false,
    val isLoading: Boolean = true,
    val cycleStatistics: CycleStatistics? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPredictionUseCase: GetPredictionUseCase,
    private val getCurrentCycleUseCase: GetCurrentCycleUseCase,
    private val getAllCyclesUseCase: GetAllCyclesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun computeStatistics(cycles: List<Cycle>): CycleStatistics? {
        val completedCycles = cycles.filter { it.cycleLength != null }
        val cycleLengths = completedCycles.mapNotNull { it.cycleLength }
        if (cycleLengths.size < 2) return null

        val periodLengths = completedCycles.mapNotNull { it.periodLength }
        val avgCycle = cycleLengths.average().roundToInt()
        val avgPeriod = if (periodLengths.isNotEmpty()) periodLengths.average().roundToInt() else 0
        val mean = cycleLengths.average()
        val variance = cycleLengths.map { (it - mean) * (it - mean) }.average()
        val stdDev = sqrt(variance)
        val regularity = when {
            stdDev <= 2.0 -> "Regular"
            stdDev <= 4.0 -> "Somewhat Regular"
            else -> "Irregular"
        }

        return CycleStatistics(
            averageCycleLength = avgCycle,
            averagePeriodLength = avgPeriod,
            shortestCycle = cycleLengths.min(),
            longestCycle = cycleLengths.max(),
            totalCyclesLogged = cycleLengths.size,
            regularity = regularity
        )
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val prediction = getPredictionUseCase()
                val currentCycle = getCurrentCycleUseCase()
                getAllCyclesUseCase().first().let { cycles ->
                    val stats = computeStatistics(cycles)
                    _uiState.update {
                        it.copy(
                            prediction = prediction,
                            currentCycle = currentCycle,
                            hasCycles = cycles.isNotEmpty(),
                            isLoading = false,
                            cycleStatistics = stats
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
