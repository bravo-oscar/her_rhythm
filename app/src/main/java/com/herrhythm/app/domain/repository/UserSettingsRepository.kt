package com.herrhythm.app.domain.repository

import com.herrhythm.app.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getSettings(): Flow<UserSettings>
    suspend fun getSettingsOnce(): UserSettings
    suspend fun updateSettings(settings: UserSettings)
}
