package com.herrhythm.app.domain.usecase.settings

import com.herrhythm.app.domain.model.UserSettings
import com.herrhythm.app.domain.repository.UserSettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) {
    suspend operator fun invoke(settings: UserSettings) {
        userSettingsRepository.updateSettings(settings)
    }
}
