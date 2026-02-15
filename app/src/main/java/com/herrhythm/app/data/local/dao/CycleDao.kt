package com.herrhythm.app.data.local.dao

import androidx.room.*
import com.herrhythm.app.data.local.entity.CycleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CycleDao {

    @Insert
    suspend fun insert(cycle: CycleEntity): Long

    @Update
    suspend fun update(cycle: CycleEntity)

    @Delete
    suspend fun delete(cycle: CycleEntity)

    @Query("SELECT * FROM cycles ORDER BY start_date DESC")
    fun getAllCycles(): Flow<List<CycleEntity>>

    @Query("SELECT * FROM cycles WHERE id = :id")
    suspend fun getCycleById(id: Long): CycleEntity?

    @Query("SELECT * FROM cycles WHERE cycle_length IS NOT NULL ORDER BY start_date DESC LIMIT :limit")
    suspend fun getCompletedCycles(limit: Int): List<CycleEntity>

    @Query("SELECT * FROM cycles ORDER BY start_date DESC LIMIT 1")
    suspend fun getMostRecentCycle(): CycleEntity?

    @Query("SELECT * FROM cycles WHERE start_date <= :date AND (end_date IS NULL OR end_date >= :date) LIMIT 1")
    suspend fun getCycleForDate(date: LocalDate): CycleEntity?

    @Query("DELETE FROM cycles")
    suspend fun deleteAll()
}
