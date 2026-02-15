package com.herrhythm.app.domain.usecase.log

import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.repository.DailyLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLogsUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository
) {
    operator fun invoke(): Flow<List<DailyLog>> {
        return dailyLogRepository.getAllLogs()
    }
}
