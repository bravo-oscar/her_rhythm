package com.herrhythm.app.data.repository

import com.herrhythm.app.data.local.dao.DailyLogDao
import com.herrhythm.app.data.local.entity.DailyLogEntity
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.repository.DailyLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyLogRepositoryImpl @Inject constructor(
    private val dailyLogDao: DailyLogDao
) : DailyLogRepository {

    override suspend fun insertLog(log: DailyLog): Long {
        return dailyLogDao.insert(log.toEntity())
    }

    override suspend fun updateLog(log: DailyLog) {
        dailyLogDao.update(log.toEntity())
    }

    override suspend fun deleteLog(log: DailyLog) {
        dailyLogDao.delete(log.toEntity())
    }

    override fun getAllLogs(): Flow<List<DailyLog>> {
        return dailyLogDao.getAllLogs().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getLogByDate(date: LocalDate): DailyLog? {
        return dailyLogDao.getLogByDate(date)?.toDomain()
    }

    override suspend fun getLogsBetween(start: LocalDate, end: LocalDate): List<DailyLog> {
        return dailyLogDao.getLogsBetween(start, end).map { it.toDomain() }
    }

    override suspend fun getLogsForCycle(cycleId: Long): List<DailyLog> {
        return dailyLogDao.getLogsForCycle(cycleId).map { it.toDomain() }
    }

    override suspend fun deleteAll() {
        dailyLogDao.deleteAll()
    }
}

private fun DailyLogEntity.toDomain(): DailyLog {
    return DailyLog(
        id = id,
        date = date,
        cycleId = cycleId,
        note = note,
        mood = mood,
        symptoms = symptoms,
        flowIntensity = flowIntensity,
        temperature = temperature,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun DailyLog.toEntity(): DailyLogEntity {
    return DailyLogEntity(
        id = id,
        date = date,
        cycleId = cycleId,
        note = note,
        mood = mood,
        symptoms = symptoms,
        flowIntensity = flowIntensity,
        temperature = temperature,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
