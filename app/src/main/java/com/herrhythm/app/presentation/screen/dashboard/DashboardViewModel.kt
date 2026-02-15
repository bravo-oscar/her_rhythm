package com.herrhythm.app.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.Prediction
import com.herrhythm.app.domain.usecase.cycle.GetAllCyclesUseCase
import com.herrhythm.app.domain.usecase.cycle.GetCurrentCycleUseCase
import com.herrhythm.app.domain.usecase.prediction.GetPredictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val prediction: Prediction? = null,
    val currentCycle: Cycle? = null,
    val hasCycles: Boolean = false,
    val isLoading: Boolean = true
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

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val prediction = getPredictionUseCase()
                val currentCycle = getCurrentCycleUseCase()
                getAllCyclesUseCase().first().let { cycles ->
                    _uiState.update {
                        it.copy(
                            prediction = prediction,
                            currentCycle = currentCycle,
                            hasCycles = cycles.isNotEmpty(),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
