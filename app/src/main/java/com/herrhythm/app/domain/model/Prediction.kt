package com.herrhythm.app.domain.model

import java.time.LocalDate

data class Prediction(
    val predictedCycleLength: Int,
    val nextPeriodStart: LocalDate,
    val ovulationDate: LocalDate,
    val fertileWindowStart: LocalDate,
    val fertileWindowEnd: LocalDate,
    val pmsWindowStart: LocalDate,
    val currentPhase: CyclePhase,
    val currentCycleDay: Int,
    val confidence: Float,
    val confidenceLevel: ConfidenceLevel
)
