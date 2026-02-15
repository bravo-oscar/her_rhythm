package com.herrhythm.app.data.repository

import com.herrhythm.app.data.local.dao.UserSettingsDao
import com.herrhythm.app.data.local.entity.UserSettingsEntity
import com.herrhythm.app.domain.model.UserSettings
import com.herrhythm.app.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepositoryImpl @Inject constructor(
    private val userSettingsDao: UserSettingsDao
) : UserSettingsRepository {

    override fun getSettings(): Flow<UserSettings> {
        return userSettingsDao.getSettings().map { entity ->
            entity?.toDomain() ?: UserSettings()
        }
    }

    override suspend fun getSettingsOnce(): UserSettings {
        return userSettingsDao.getSettingsOnce()?.toDomain() ?: UserSettings()
    }

    override suspend fun updateSettings(settings: UserSettings) {
        userSettingsDao.insertOrUpdate(settings.toEntity())
    }
}

private fun UserSettingsEntity.toDomain(): UserSettings {
    return UserSettings(
        id = id,
        pmsWindowDays = pmsWindowDays,
        defaultCycleLength = defaultCycleLength,
        defaultPeriodLength = defaultPeriodLength,
        predictionCycleCount = predictionCycleCount,
        notifyBeforePeriod = notifyBeforePeriod,
        notifyBeforePeriodDays = notifyBeforePeriodDays,
        notifyPmsOnset = notifyPmsOnset,
        dailyReminderEnabled = dailyReminderEnabled,
        dailyReminderHour = dailyReminderHour,
        dailyReminderMinute = dailyReminderMinute,
        fertileWindowEnabled = fertileWindowEnabled,
        themeMode = themeMode
    )
}

private fun UserSettings.toEntity(): UserSettingsEntity {
    return UserSettingsEntity(
        id = id,
        pmsWindowDays = pmsWindowDays,
        defaultCycleLength = defaultCycleLength,
        defaultPeriodLength = defaultPeriodLength,
        predictionCycleCount = predictionCycleCount,
        notifyBeforePeriod = notifyBeforePeriod,
        notifyBeforePeriodDays = notifyBeforePeriodDays,
        notifyPmsOnset = notifyPmsOnset,
        dailyReminderEnabled = dailyReminderEnabled,
        dailyReminderHour = dailyReminderHour,
        dailyReminderMinute = dailyReminderMinute,
        fertileWindowEnabled = fertileWindowEnabled,
        themeMode = themeMode
    )
}
