package com.herrhythm.app.data.local

import com.herrhythm.app.data.local.dao.CycleDao
import com.herrhythm.app.data.local.dao.DailyLogDao
import com.herrhythm.app.data.local.entity.CycleEntity
import com.herrhythm.app.data.local.entity.DailyLogEntity
import com.herrhythm.app.domain.model.FlowIntensity
import com.herrhythm.app.domain.model.Mood
import com.herrhythm.app.domain.model.Symptom
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSeeder @Inject constructor(
    private val cycleDao: CycleDao,
    private val dailyLogDao: DailyLogDao
) {
    /**
     * Seeds ~6 months of realistic cycle data with daily logs.
     * Clears existing data first.
     */
    suspend fun seedDemoData() {
        // Clear existing data
        dailyLogDao.deleteAll()
        cycleDao.deleteAll()

        val today = LocalDate.now()

        // 6 completed cycles with realistic variation, plus 1 current open cycle
        // Working backwards from today
        data class CycleDef(val length: Int, val periodDays: Int, val flow: FlowIntensity)

        val cycleDefs = listOf(
            CycleDef(29, 5, FlowIntensity.MEDIUM),
            CycleDef(27, 4, FlowIntensity.HEAVY),
            CycleDef(30, 5, FlowIntensity.MEDIUM),
            CycleDef(28, 5, FlowIntensity.LIGHT),
            CycleDef(31, 6, FlowIntensity.HEAVY),
            CycleDef(28, 5, FlowIntensity.MEDIUM)
        )

        // Calculate start dates going backwards
        // Current cycle started some days ago (day 12 of current cycle)
        val currentCycleStart = today.minusDays(11)

        // Build completed cycles backwards from current cycle start
        val completedStarts = mutableListOf<LocalDate>()
        var cursor = currentCycleStart
        for (def in cycleDefs) {
            cursor = cursor.minusDays(def.length.toLong())
            completedStarts.add(cursor)
        }
        completedStarts.reverse() // oldest first

        val cycleIds = mutableListOf<Long>()

        // Insert completed cycles
        for (i in cycleDefs.indices.reversed()) {
            val def = cycleDefs[i]
            val startDate = completedStarts[cycleDefs.size - 1 - i]
            val endDate = startDate.plusDays(def.length.toLong() - 1)

            val id = cycleDao.insert(
                CycleEntity(
                    startDate = startDate,
                    endDate = endDate,
                    cycleLength = def.length,
                    periodLength = def.periodDays,
                    flowIntensity = def.flow,
                    notes = "",
                    createdAt = startDate,
                    updatedAt = startDate
                )
            )
            cycleIds.add(id)
        }

        // Insert current (open) cycle
        val currentCycleId = cycleDao.insert(
            CycleEntity(
                startDate = currentCycleStart,
                endDate = null,
                cycleLength = null,
                periodLength = null,
                flowIntensity = FlowIntensity.MEDIUM,
                notes = "",
                createdAt = currentCycleStart,
                updatedAt = today
            )
        )

        // Generate daily logs for each completed cycle
        for (i in cycleDefs.indices.reversed()) {
            val def = cycleDefs[i]
            val idx = cycleDefs.size - 1 - i
            val startDate = completedStarts[idx]
            val cycleId = cycleIds[idx]

            for (day in 0 until def.length) {
                val date = startDate.plusDays(day.toLong())
                val dayInCycle = day + 1
                val log = buildDailyLog(date, cycleId, dayInCycle, def.periodDays, def.length)
                dailyLogDao.insert(log)
            }
        }

        // Daily logs for current cycle (up to today)
        val currentDays = java.time.temporal.ChronoUnit.DAYS.between(currentCycleStart, today).toInt() + 1
        for (day in 0 until currentDays) {
            val date = currentCycleStart.plusDays(day.toLong())
            val log = buildDailyLog(date, currentCycleId, day + 1, 5, 28)
            dailyLogDao.insert(log)
        }
    }

    private fun buildDailyLog(
        date: LocalDate,
        cycleId: Long,
        dayInCycle: Int,
        periodDays: Int,
        cycleLength: Int
    ): DailyLogEntity {
        val inPeriod = dayInCycle <= periodDays
        val ovulationWindow = dayInCycle in (cycleLength - 16)..(cycleLength - 12)
        val luteal = dayInCycle > (cycleLength - 14)
        val prePeriod = dayInCycle >= (cycleLength - 3)

        val flowIntensity = when {
            !inPeriod -> FlowIntensity.NONE
            dayInCycle == 1 -> FlowIntensity.LIGHT
            dayInCycle == 2 || dayInCycle == 3 -> FlowIntensity.HEAVY
            dayInCycle == periodDays -> FlowIntensity.LIGHT
            else -> FlowIntensity.MEDIUM
        }

        val mood = when {
            inPeriod && dayInCycle <= 2 -> Mood.TIRED
            inPeriod -> Mood.SENSITIVE
            ovulationWindow -> Mood.ENERGETIC
            prePeriod -> Mood.IRRITABLE
            luteal && dayInCycle % 3 == 0 -> Mood.ANXIOUS
            luteal -> Mood.CALM
            else -> listOf(Mood.HAPPY, Mood.CALM, Mood.ENERGETIC, Mood.CONFIDENT)[dayInCycle % 4]
        }

        val symptoms = buildList {
            if (inPeriod) {
                add(Symptom.CRAMPS)
                if (dayInCycle <= 2) add(Symptom.FATIGUE)
                if (dayInCycle <= 3) add(Symptom.BLOATING)
            }
            if (prePeriod) {
                add(Symptom.MOOD_SWINGS)
                add(Symptom.BREAST_TENDERNESS)
                if (dayInCycle % 2 == 0) add(Symptom.CRAVINGS)
            }
            if (luteal && !prePeriod && dayInCycle % 4 == 0) {
                add(Symptom.HEADACHE)
            }
            if (ovulationWindow && dayInCycle % 2 == 0) {
                add(Symptom.ABDOMINAL_PAIN)
            }
        }

        // Slight temperature variation simulating BBT pattern
        val baseTemp = when {
            inPeriod -> 36.3f
            ovulationWindow -> 36.2f
            luteal -> 36.6f
            else -> 36.4f
        }
        val tempVariation = ((date.dayOfYear % 5) - 2) * 0.05f
        val temperature = baseTemp + tempVariation

        return DailyLogEntity(
            date = date,
            cycleId = cycleId,
            note = "",
            mood = mood,
            symptoms = symptoms,
            flowIntensity = flowIntensity,
            temperature = temperature,
            lifestyleFactors = emptyList(),
            createdAt = date,
            updatedAt = date
        )
    }

    suspend fun clearAllData() {
        dailyLogDao.deleteAll()
        cycleDao.deleteAll()
    }
}
