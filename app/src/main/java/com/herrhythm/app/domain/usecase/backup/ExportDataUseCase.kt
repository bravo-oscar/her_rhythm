package com.herrhythm.app.domain.usecase.backup

import android.content.Context
import android.net.Uri
import com.herrhythm.app.domain.repository.CycleRepository
import com.herrhythm.app.domain.repository.DailyLogRepository
import com.herrhythm.app.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExportDataUseCase @Inject constructor(
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val settingsRepository: UserSettingsRepository
) {
    private val json = Json { prettyPrint = true }

    suspend operator fun invoke(context: Context, uri: Uri) {
        val cycles = cycleRepository.getAllCycles().first()
        val logs = dailyLogRepository.getAllLogs().first()
        val settings = settingsRepository.getSettingsOnce()

        val backupData = BackupData(
            cycles = cycles.map { cycle ->
                BackupCycle(
                    startDate = cycle.startDate.toString(),
                    endDate = cycle.endDate?.toString(),
                    cycleLength = cycle.cycleLength,
                    periodLength = cycle.periodLength,
                    flowIntensity = cycle.flowIntensity.name,
                    notes = cycle.notes
                )
            },
            dailyLogs = logs.map { log ->
                BackupDailyLog(
                    date = log.date.toString(),
                    note = log.note,
                    mood = log.mood.name,
                    symptoms = log.symptoms.map { it.name },
                    flowIntensity = log.flowIntensity.name,
                    temperature = log.temperature
                )
            },
            settings = BackupSettings(
                pmsWindowDays = settings.pmsWindowDays,
                defaultCycleLength = settings.defaultCycleLength,
                defaultPeriodLength = settings.defaultPeriodLength,
                predictionCycleCount = settings.predictionCycleCount,
                notifyBeforePeriod = settings.notifyBeforePeriod,
                notifyBeforePeriodDays = settings.notifyBeforePeriodDays,
                notifyPmsOnset = settings.notifyPmsOnset,
                dailyReminderEnabled = settings.dailyReminderEnabled,
                dailyReminderHour = settings.dailyReminderHour,
                dailyReminderMinute = settings.dailyReminderMinute,
                fertileWindowEnabled = settings.fertileWindowEnabled,
                themeMode = settings.themeMode
            )
        )

        val jsonString = json.encodeToString(backupData)
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    }
}
