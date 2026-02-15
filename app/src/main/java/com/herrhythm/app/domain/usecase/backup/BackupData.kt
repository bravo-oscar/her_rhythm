package com.herrhythm.app.domain.usecase.backup

import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val version: Int = 1,
    val cycles: List<BackupCycle>,
    val dailyLogs: List<BackupDailyLog>,
    val settings: BackupSettings?
)

@Serializable
data class BackupCycle(
    val startDate: String,
    val endDate: String?,
    val cycleLength: Int?,
    val periodLength: Int?,
    val flowIntensity: String,
    val notes: String
)

@Serializable
data class BackupDailyLog(
    val date: String,
    val note: String,
    val mood: String,
    val symptoms: List<String>,
    val flowIntensity: String,
    val temperature: Float?,
    val lifestyleFactors: List<String> = emptyList()
)

@Serializable
data class BackupSettings(
    val pmsWindowDays: Int,
    val defaultCycleLength: Int,
    val defaultPeriodLength: Int,
    val predictionCycleCount: Int,
    val notifyBeforePeriod: Boolean,
    val notifyBeforePeriodDays: Int,
    val notifyPmsOnset: Boolean,
    val dailyReminderEnabled: Boolean,
    val dailyReminderHour: Int,
    val dailyReminderMinute: Int,
    val fertileWindowEnabled: Boolean,
    val themeMode: String
)
