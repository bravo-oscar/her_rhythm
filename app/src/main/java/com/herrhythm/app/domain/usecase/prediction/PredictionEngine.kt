package com.herrhythm.app.domain.usecase.prediction

import com.herrhythm.app.domain.model.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt
import kotlin.math.min

@Singleton
class PredictionEngine @Inject constructor() {

    fun predict(
        completedCycles: List<Cycle>,
        mostRecentCycle: Cycle?,
        settings: UserSettings
    ): Prediction {
        val today = LocalDate.now()

        val cycleLengths = completedCycles
            .filter { it.cycleLength != null }
            .take(settings.predictionCycleCount)
            .mapNotNull { it.cycleLength }

        val predictedLength = calculateWeightedAverage(cycleLengths, settings.defaultCycleLength)

        val referenceStart = mostRecentCycle?.startDate ?: today
        val nextPeriodStart = referenceStart.plusDays(predictedLength.toLong())

        val ovulationDate = nextPeriodStart.minusDays(14)
        val fertileWindowStart = ovulationDate.minusDays(5)
        val fertileWindowEnd = ovulationDate.plusDays(1)
        val pmsWindowStart = nextPeriodStart.minusDays(settings.pmsWindowDays.toLong())

        val currentCycleDay = if (mostRecentCycle != null) {
            ChronoUnit.DAYS.between(mostRecentCycle.startDate, today).toInt() + 1
        } else 0

        val periodLength = mostRecentCycle?.periodLength ?: settings.defaultPeriodLength
        val currentPhase = determinePhase(currentCycleDay, periodLength, predictedLength, settings)

        val confidence = calculateConfidence(cycleLengths)

        return Prediction(
            predictedCycleLength = predictedLength,
            nextPeriodStart = nextPeriodStart,
            ovulationDate = ovulationDate,
            fertileWindowStart = fertileWindowStart,
            fertileWindowEnd = fertileWindowEnd,
            pmsWindowStart = pmsWindowStart,
            currentPhase = currentPhase,
            currentCycleDay = currentCycleDay,
            confidence = confidence,
            confidenceLevel = ConfidenceLevel.fromScore(confidence)
        )
    }

    fun calculateWeightedAverage(lengths: List<Int>, default: Int): Int {
        if (lengths.isEmpty()) return default
        if (lengths.size == 1) return lengths.first()

        // Oldest to newest - assign linearly increasing weights
        val reversed = lengths.reversed() // oldest first
        var weightedSum = 0.0
        var totalWeight = 0.0
        for (i in reversed.indices) {
            val weight = (i + 1).toDouble()
            weightedSum += weight * reversed[i]
            totalWeight += weight
        }
        return (weightedSum / totalWeight).toInt()
    }

    fun calculateConfidence(lengths: List<Int>): Float {
        if (lengths.isEmpty()) return 0f
        if (lengths.size == 1) return 0.15f

        // 60% weight: data quantity (plateaus at 6+)
        val dataScore = min(lengths.size, 6) / 6.0f

        // 40% weight: regularity (low std deviation = higher)
        val mean = lengths.average()
        val variance = lengths.map { (it - mean) * (it - mean) }.average()
        val stdDev = sqrt(variance)
        val regularityScore = when {
            stdDev <= 1.0 -> 1.0f
            stdDev <= 2.0 -> 0.8f
            stdDev <= 3.0 -> 0.6f
            stdDev <= 5.0 -> 0.4f
            else -> 0.2f
        }

        val raw = (0.6f * dataScore) + (0.4f * regularityScore)
        return min(raw, 0.95f)
    }

    fun determinePhase(
        cycleDay: Int,
        periodLength: Int,
        cycleLength: Int,
        settings: UserSettings
    ): CyclePhase {
        if (cycleDay <= 0) return CyclePhase.UNKNOWN

        return when {
            cycleDay <= periodLength -> CyclePhase.MENSTRUAL
            cycleDay <= cycleLength - 14 - 2 -> CyclePhase.FOLLICULAR
            cycleDay <= cycleLength - 14 + 2 -> CyclePhase.OVULATION
            else -> CyclePhase.LUTEAL
        }
    }
}
