package com.herrhythm.app.domain.usecase.prediction

import com.herrhythm.app.domain.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class PredictionEngineTest {

    private lateinit var engine: PredictionEngine
    private val defaultSettings = UserSettings()

    @Before
    fun setup() {
        engine = PredictionEngine()
    }

    // --- Weighted Average Tests ---

    @Test
    fun `empty list returns default cycle length`() {
        assertEquals(28, engine.calculateWeightedAverage(emptyList(), 28))
    }

    @Test
    fun `single cycle returns that length`() {
        assertEquals(30, engine.calculateWeightedAverage(listOf(30), 28))
    }

    @Test
    fun `two equal cycles returns that length`() {
        assertEquals(28, engine.calculateWeightedAverage(listOf(28, 28), 28))
    }

    @Test
    fun `weighted average favors recent cycles`() {
        // Cycles from newest to oldest: 30, 26
        // Reversed (oldest first): 26, 30
        // Weights: 1*26 + 2*30 = 86 / 3 = 28.67 -> 28
        val result = engine.calculateWeightedAverage(listOf(30, 26), 28)
        assertEquals(28, result)
    }

    @Test
    fun `weighted average with three cycles`() {
        // Newest to oldest: 32, 28, 26
        // Reversed: 26, 28, 32
        // 1*26 + 2*28 + 3*32 = 26+56+96 = 178 / 6 = 29.67 -> 29
        val result = engine.calculateWeightedAverage(listOf(32, 28, 26), 28)
        assertEquals(29, result)
    }

    @Test
    fun `weighted average with six regular cycles`() {
        val lengths = listOf(28, 29, 28, 29, 28, 29) // newest first
        val result = engine.calculateWeightedAverage(lengths, 28)
        // Should be close to 28-29
        assertTrue(result in 28..29)
    }

    // --- Confidence Tests ---

    @Test
    fun `confidence is zero with no data`() {
        assertEquals(0f, engine.calculateConfidence(emptyList()), 0.001f)
    }

    @Test
    fun `confidence is low with one cycle`() {
        assertEquals(0.15f, engine.calculateConfidence(listOf(28)), 0.001f)
    }

    @Test
    fun `confidence increases with more regular data`() {
        val conf2 = engine.calculateConfidence(listOf(28, 28))
        val conf6 = engine.calculateConfidence(listOf(28, 28, 28, 28, 28, 28))
        assertTrue(conf6 > conf2)
    }

    @Test
    fun `confidence is lower with irregular cycles`() {
        val regular = engine.calculateConfidence(listOf(28, 28, 28, 28, 28, 28))
        val irregular = engine.calculateConfidence(listOf(22, 35, 28, 40, 25, 33))
        assertTrue(regular > irregular)
    }

    @Test
    fun `confidence never exceeds 0_95`() {
        val conf = engine.calculateConfidence(listOf(28, 28, 28, 28, 28, 28, 28, 28, 28, 28))
        assertTrue(conf <= 0.95f)
    }

    @Test
    fun `confidence level LOW for low score`() {
        assertEquals(ConfidenceLevel.LOW, ConfidenceLevel.fromScore(0.1f))
    }

    @Test
    fun `confidence level MODERATE for moderate score`() {
        assertEquals(ConfidenceLevel.MODERATE, ConfidenceLevel.fromScore(0.3f))
    }

    @Test
    fun `confidence level GOOD for good score`() {
        assertEquals(ConfidenceLevel.GOOD, ConfidenceLevel.fromScore(0.6f))
    }

    @Test
    fun `confidence level HIGH for high score`() {
        assertEquals(ConfidenceLevel.HIGH, ConfidenceLevel.fromScore(0.8f))
    }

    // --- Phase Detection Tests ---

    @Test
    fun `day 1 is menstrual phase`() {
        assertEquals(CyclePhase.MENSTRUAL, engine.determinePhase(1, 5, 28, defaultSettings))
    }

    @Test
    fun `day 5 is menstrual phase`() {
        assertEquals(CyclePhase.MENSTRUAL, engine.determinePhase(5, 5, 28, defaultSettings))
    }

    @Test
    fun `day 8 is follicular phase`() {
        assertEquals(CyclePhase.FOLLICULAR, engine.determinePhase(8, 5, 28, defaultSettings))
    }

    @Test
    fun `day 14 is ovulation phase`() {
        assertEquals(CyclePhase.OVULATION, engine.determinePhase(14, 5, 28, defaultSettings))
    }

    @Test
    fun `day 20 is luteal phase`() {
        assertEquals(CyclePhase.LUTEAL, engine.determinePhase(20, 5, 28, defaultSettings))
    }

    @Test
    fun `day 0 is unknown phase`() {
        assertEquals(CyclePhase.UNKNOWN, engine.determinePhase(0, 5, 28, defaultSettings))
    }

    // --- Full Prediction Tests ---

    @Test
    fun `prediction with no cycles uses defaults`() {
        val prediction = engine.predict(emptyList(), null, defaultSettings)
        assertEquals(28, prediction.predictedCycleLength)
        assertEquals(0, prediction.currentCycleDay)
        assertEquals(CyclePhase.UNKNOWN, prediction.currentPhase)
        assertEquals(0f, prediction.confidence, 0.001f)
    }

    @Test
    fun `prediction with one completed cycle`() {
        val cycle = Cycle(
            id = 1,
            startDate = LocalDate.now().minusDays(30),
            endDate = LocalDate.now().minusDays(25),
            cycleLength = 30,
            periodLength = 5
        )
        val prediction = engine.predict(listOf(cycle), cycle, defaultSettings)
        assertEquals(30, prediction.predictedCycleLength)
        assertTrue(prediction.currentCycleDay > 0)
    }

    @Test
    fun `prediction calculates fertile window correctly`() {
        val cycle = Cycle(
            id = 1,
            startDate = LocalDate.now().minusDays(5),
            endDate = LocalDate.now(),
            cycleLength = 28,
            periodLength = 5
        )
        val prediction = engine.predict(listOf(cycle), cycle, defaultSettings)
        // Fertile window: ovulation - 5 to ovulation + 1
        // Ovulation = nextPeriodStart - 14
        assertEquals(prediction.ovulationDate.minusDays(5), prediction.fertileWindowStart)
        assertEquals(prediction.ovulationDate.plusDays(1), prediction.fertileWindowEnd)
    }

    @Test
    fun `prediction calculates PMS window from settings`() {
        val settings = defaultSettings.copy(pmsWindowDays = 10)
        val cycle = Cycle(
            id = 1,
            startDate = LocalDate.now().minusDays(5),
            cycleLength = 28,
            periodLength = 5
        )
        val prediction = engine.predict(listOf(cycle), cycle, settings)
        assertEquals(prediction.nextPeriodStart.minusDays(10), prediction.pmsWindowStart)
    }
}
