package com.herrhythm.app.domain.model

data class UserSettings(
    val id: Int = 1,
    val pmsWindowDays: Int = 7,
    val defaultCycleLength: Int = 28,
    val defaultPeriodLength: Int = 5,
    val predictionCycleCount: Int = 6,
    val notifyBeforePeriod: Boolean = true,
    val notifyBeforePeriodDays: Int = 2,
    val notifyPmsOnset: Boolean = true,
    val dailyReminderEnabled: Boolean = false,
    val dailyReminderHour: Int = 20,
    val dailyReminderMinute: Int = 0,
    val fertileWindowEnabled: Boolean = true,
    val themeMode: String = "system"
)
