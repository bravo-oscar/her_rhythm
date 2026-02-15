package com.herrhythm.app.domain.usecase.settings

import com.herrhythm.app.domain.model.UserSettings
import com.herrhythm.app.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) {
    operator fun invoke(): Flow<UserSettings> {
        return userSettingsRepository.getSettings()
    }
}
