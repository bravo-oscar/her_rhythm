package com.herrhythm.app.domain.usecase.log

import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.repository.DailyLogRepository
import javax.inject.Inject

class AddDailyLogUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository
) {
    suspend operator fun invoke(log: DailyLog): Long {
        return dailyLogRepository.insertLog(log)
    }
}
