package com.herrhythm.app.domain.repository

import com.herrhythm.app.domain.model.Cycle
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CycleRepository {
    fun getAllCycles(): Flow<List<Cycle>>
    suspend fun getCycleById(id: Long): Cycle?
    suspend fun getCompletedCycles(limit: Int): List<Cycle>
    suspend fun getMostRecentCycle(): Cycle?
    suspend fun getCycleForDate(date: LocalDate): Cycle?
    suspend fun insertCycle(cycle: Cycle): Long
    suspend fun updateCycle(cycle: Cycle)
    suspend fun deleteCycle(cycle: Cycle)
}
