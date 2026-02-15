package com.herrhythm.app.domain.usecase.log

import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.repository.DailyLogRepository
import java.time.LocalDate
import javax.inject.Inject

class GetLogByDateUseCase @Inject constructor(
    private val dailyLogRepository: DailyLogRepository
) {
    suspend operator fun invoke(date: LocalDate): DailyLog? {
        return dailyLogRepository.getLogByDate(date)
    }
}
