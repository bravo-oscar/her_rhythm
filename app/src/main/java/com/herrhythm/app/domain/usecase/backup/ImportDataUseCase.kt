package com.herrhythm.app.domain.usecase.backup

import android.content.Context
import android.net.Uri
import com.herrhythm.app.domain.model.*
import com.herrhythm.app.domain.repository.CycleRepository
import com.herrhythm.app.domain.repository.DailyLogRepository
import com.herrhythm.app.domain.repository.UserSettingsRepository
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject

class ImportDataUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val settingsRepository: UserSettingsRepository
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend operator fun invoke(context: Context, uri: Uri) {
        val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().readText()
        } ?: throw IllegalStateException("Could not read file")

        val backupData = json.decodeFromString<BackupData>(jsonString)

        // Clear existing data
        dailyLogRepository.deleteAll()

        // Import cycles
        for (backup in backupData.cycles) {
            val cycle = Cycle(
                startDate = LocalDate.parse(backup.startDate),
                endDate = backup.endDate?.let { LocalDate.parse(it) },
                cycleLength = backup.cycleLength,
                periodLength = backup.periodLength,
                flowIntensity = try { FlowIntensity.valueOf(backup.flowIntensity) } catch (e: Exception) { FlowIntensity.MEDIUM },
                notes = backup.notes
            )
            cycleRepository.insertCycle(cycle)
        }

        // Import daily logs
        for (backup in backupData.dailyLogs) {
            val log = DailyLog(
                date = LocalDate.parse(backup.date),
                note = backup.note,
                mood = try { Mood.valueOf(backup.mood) } catch (e: Exception) { Mood.NEUTRAL },
                symptoms = backup.symptoms.mapNotNull { name ->
                    try { Symptom.valueOf(name) } catch (e: Exception) { null }
                },
                flowIntensity = try { FlowIntensity.valueOf(backup.flowIntensity) } catch (e: Exception) { FlowIntensity.NONE },
                temperature = backup.temperature
            )
            dailyLogRepository.insertLog(log)
        }

        // Import settings
        backupData.settings?.let { s ->
            settingsRepository.updateSettings(
                UserSettings(
                    pmsWindowDays = s.pmsWindowDays,
                    defaultCycleLength = s.defaultCycleLength,
                    defaultPeriodLength = s.defaultPeriodLength,
                    predictionCycleCount = s.predictionCycleCount,
                    notifyBeforePeriod = s.notifyBeforePeriod,
                    notifyBeforePeriodDays = s.notifyBeforePeriodDays,
                    notifyPmsOnset = s.notifyPmsOnset,
                    dailyReminderEnabled = s.dailyReminderEnabled,
                    dailyReminderHour = s.dailyReminderHour,
                    dailyReminderMinute = s.dailyReminderMinute,
                    fertileWindowEnabled = s.fertileWindowEnabled,
                    themeMode = s.themeMode
                )
            )
        }
    }
}
