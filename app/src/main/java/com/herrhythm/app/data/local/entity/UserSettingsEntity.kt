package com.herrhythm.app.data.local.entity

import androidx.room.*

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "pms_window_days") val pmsWindowDays: Int = 7,
    @ColumnInfo(name = "default_cycle_length") val defaultCycleLength: Int = 28,
    @ColumnInfo(name = "default_period_length") val defaultPeriodLength: Int = 5,
    @ColumnInfo(name = "prediction_cycle_count") val predictionCycleCount: Int = 6,
    @ColumnInfo(name = "notify_before_period") val notifyBeforePeriod: Boolean = true,
    @ColumnInfo(name = "notify_before_period_days") val notifyBeforePeriodDays: Int = 2,
    @ColumnInfo(name = "notify_pms_onset") val notifyPmsOnset: Boolean = true,
    @ColumnInfo(name = "daily_reminder_enabled") val dailyReminderEnabled: Boolean = false,
    @ColumnInfo(name = "daily_reminder_hour") val dailyReminderHour: Int = 20,
    @ColumnInfo(name = "daily_reminder_minute") val dailyReminderMinute: Int = 0,
    @ColumnInfo(name = "fertile_window_enabled") val fertileWindowEnabled: Boolean = true,
    @ColumnInfo(name = "theme_mode") val themeMode: String = "system"
)
