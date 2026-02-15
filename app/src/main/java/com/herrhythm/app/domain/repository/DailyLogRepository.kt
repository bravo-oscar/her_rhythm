package com.herrhythm.app.domain.repository

import com.herrhythm.app.domain.model.DailyLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DailyLogRepository {
    fun getAllLogs(): Flow<List<DailyLog>>
    suspend fun getLogByDate(date: LocalDate): DailyLog?
    suspend fun getLogsBetween(start: LocalDate, end: LocalDate): List<DailyLog>
    suspend fun getLogsForCycle(cycleId: Long): List<DailyLog>
    suspend fun insertLog(log: DailyLog): Long
    suspend fun updateLog(log: DailyLog)
    suspend fun deleteLog(log: DailyLog)
    suspend fun deleteAll()
}
