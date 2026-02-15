package com.herrhythm.app.presentation.screen.logentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.model.FlowIntensity
import com.herrhythm.app.domain.model.LifestyleFactor
import com.herrhythm.app.domain.model.Mood
import com.herrhythm.app.domain.model.Symptom
import com.herrhythm.app.domain.usecase.log.AddDailyLogUseCase
import com.herrhythm.app.domain.usecase.log.GetLogByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DailyLogEntryUiState(
    val date: LocalDate = LocalDate.now(),
    val mood: Mood = Mood.NEUTRAL,
    val symptoms: List<Symptom> = emptyList(),
    val flowIntensity: FlowIntensity = FlowIntensity.NONE,
    val note: String = "",
    val temperature: String = "",
    val lifestyleFactors: List<LifestyleFactor> = emptyList(),
    val isEditing: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class DailyLogEntryViewModel @Inject constructor(
    private val addDailyLogUseCase: AddDailyLogUseCase,
    private val getLogByDateUseCase: GetLogByDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyLogEntryUiState())
    val uiState: StateFlow<DailyLogEntryUiState> = _uiState.asStateFlow()

    fun loadForDate(dateString: String) {
        val date = if (dateString.isNotBlank()) {
            try { LocalDate.parse(dateString) } catch (e: Exception) { LocalDate.now() }
        } else LocalDate.now()

        _uiState.update { it.copy(date = date) }

        viewModelScope.launch {
            getLogByDateUseCase(date)?.let { log ->
                _uiState.update {
                    it.copy(
                        mood = log.mood,
                        symptoms = log.symptoms,
                        flowIntensity = log.flowIntensity,
                        note = log.note,
                        temperature = log.temperature?.toString() ?: "",
                        lifestyleFactors = log.lifestyleFactors,
                        isEditing = true
                    )
                }
            }
        }
    }

    fun updateMood(mood: Mood) { _uiState.update { it.copy(mood = mood) } }

    fun toggleSymptom(symptom: Symptom) {
        _uiState.update { state ->
            val updated = if (symptom in state.symptoms) state.symptoms - symptom else state.symptoms + symptom
            state.copy(symptoms = updated)
        }
    }

    fun toggleLifestyleFactor(factor: LifestyleFactor) {
        _uiState.update { state ->
            val updated = if (factor in state.lifestyleFactors) state.lifestyleFactors - factor else state.lifestyleFactors + factor
            state.copy(lifestyleFactors = updated)
        }
    }

    fun updateFlowIntensity(intensity: FlowIntensity) { _uiState.update { it.copy(flowIntensity = intensity) } }
    fun updateNote(note: String) { _uiState.update { it.copy(note = note) } }
    fun updateTemperature(temp: String) { _uiState.update { it.copy(temperature = temp) } }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val temp = state.temperature.toFloatOrNull()
            val log = DailyLog(
                date = state.date,
                mood = state.mood,
                symptoms = state.symptoms,
                flowIntensity = state.flowIntensity,
                note = state.note,
                temperature = temp,
                lifestyleFactors = state.lifestyleFactors
            )
            addDailyLogUseCase(log)
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
