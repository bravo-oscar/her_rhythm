package com.herrhythm.app.data.repository

import com.herrhythm.app.data.local.dao.CycleDao
import com.herrhythm.app.data.local.entity.CycleEntity
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CycleRepositoryImpl @Inject constructor(
    private val cycleDao: CycleDao
) : CycleRepository {

    override suspend fun insertCycle(cycle: Cycle): Long {
        return cycleDao.insert(cycle.toEntity())
    }

    override suspend fun updateCycle(cycle: Cycle) {
        cycleDao.update(cycle.toEntity())
    }

    override suspend fun deleteCycle(cycle: Cycle) {
        cycleDao.delete(cycle.toEntity())
    }

    override fun getAllCycles(): Flow<List<Cycle>> {
        return cycleDao.getAllCycles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCycleById(id: Long): Cycle? {
        return cycleDao.getCycleById(id)?.toDomain()
    }

    override suspend fun getCompletedCycles(limit: Int): List<Cycle> {
        return cycleDao.getCompletedCycles(limit).map { it.toDomain() }
    }

    override suspend fun getMostRecentCycle(): Cycle? {
        return cycleDao.getMostRecentCycle()?.toDomain()
    }

    override suspend fun getCycleForDate(date: LocalDate): Cycle? {
        return cycleDao.getCycleForDate(date)?.toDomain()
    }
}

private fun CycleEntity.toDomain(): Cycle {
    return Cycle(
        id = id,
        startDate = startDate,
        endDate = endDate,
        cycleLength = cycleLength,
        periodLength = periodLength,
        flowIntensity = flowIntensity,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun Cycle.toEntity(): CycleEntity {
    return CycleEntity(
        id = id,
        startDate = startDate,
        endDate = endDate,
        cycleLength = cycleLength,
        periodLength = periodLength,
        flowIntensity = flowIntensity,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
