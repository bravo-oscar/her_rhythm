package com.herrhythm.app.domain.usecase.prediction

import com.herrhythm.app.domain.model.Prediction
import com.herrhythm.app.domain.repository.CycleRepository
import com.herrhythm.app.domain.repository.UserSettingsRepository
import javax.inject.Inject

class GetPredictionUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val settingsRepository: UserSettingsRepository,
    private val predictionEngine: PredictionEngine
) {
    suspend operator fun invoke(): Prediction {
        val settings = settingsRepository.getSettingsOnce()
        val completedCycles = cycleRepository.getCompletedCycles(settings.predictionCycleCount)
        val mostRecentCycle = cycleRepository.getMostRecentCycle()
        return predictionEngine.predict(completedCycles, mostRecentCycle, settings)
    }
}
