package com.herrhythm.app.presentation.screen.predictions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.Prediction
import com.herrhythm.app.domain.usecase.cycle.GetAllCyclesUseCase
import com.herrhythm.app.domain.usecase.prediction.GetPredictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PredictionsUiState(
    val prediction: Prediction? = null,
    val pastCycles: List<Cycle> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PredictionsViewModel @Inject constructor(
    private val getPredictionUseCase: GetPredictionUseCase,
    private val getAllCyclesUseCase: GetAllCyclesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionsUiState())
    val uiState: StateFlow<PredictionsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllCyclesUseCase().collect { cycles ->
                val prediction = try { getPredictionUseCase() } catch (e: Exception) { null }
                _uiState.update {
                    it.copy(prediction = prediction, pastCycles = cycles, isLoading = false)
                }
            }
        }
    }
}
