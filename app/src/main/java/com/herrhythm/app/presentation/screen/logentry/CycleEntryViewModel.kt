package com.herrhythm.app.presentation.screen.logentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.FlowIntensity
import com.herrhythm.app.domain.usecase.cycle.AddCycleUseCase
import com.herrhythm.app.domain.usecase.cycle.DeleteCycleUseCase
import com.herrhythm.app.domain.usecase.cycle.UpdateCycleUseCase
import com.herrhythm.app.domain.repository.CycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class CycleEntryUiState(
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate? = null,
    val flowIntensity: FlowIntensity = FlowIntensity.MEDIUM,
    val notes: String = "",
    val isEditing: Boolean = false,
    val editingCycleId: Long? = null,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
)

@HiltViewModel
class CycleEntryViewModel @Inject constructor(
    private val addCycleUseCase: AddCycleUseCase,
    private val updateCycleUseCase: UpdateCycleUseCase,
    private val deleteCycleUseCase: DeleteCycleUseCase,
    private val cycleRepository: CycleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CycleEntryUiState())
    val uiState: StateFlow<CycleEntryUiState> = _uiState.asStateFlow()

    fun loadCycle(cycleId: Long?) {
        if (cycleId == null) return
        viewModelScope.launch {
            cycleRepository.getCycleById(cycleId)?.let { cycle ->
                _uiState.update {
                    it.copy(
                        startDate = cycle.startDate,
                        endDate = cycle.endDate,
                        flowIntensity = cycle.flowIntensity,
                        notes = cycle.notes,
                        isEditing = true,
                        editingCycleId = cycle.id
                    )
                }
            }
        }
    }

    fun updateStartDate(date: LocalDate) {
        _uiState.update { it.copy(startDate = date) }
    }

    fun updateEndDate(date: LocalDate?) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun updateFlowIntensity(intensity: FlowIntensity) {
        _uiState.update { it.copy(flowIntensity = intensity) }
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val periodLength = state.endDate?.let {
                ChronoUnit.DAYS.between(state.startDate, it).toInt() + 1
            }
            val cycle = Cycle(
                id = state.editingCycleId ?: 0,
                startDate = state.startDate,
                endDate = state.endDate,
                periodLength = periodLength,
                flowIntensity = state.flowIntensity,
                notes = state.notes
            )
            if (state.isEditing) {
                updateCycleUseCase(cycle)
            } else {
                addCycleUseCase(cycle)
            }
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun delete() {
        val cycleId = _uiState.value.editingCycleId ?: return
        viewModelScope.launch {
            cycleRepository.getCycleById(cycleId)?.let { cycle ->
                deleteCycleUseCase(cycle)
                _uiState.update { it.copy(isDeleted = true) }
            }
        }
    }
}
