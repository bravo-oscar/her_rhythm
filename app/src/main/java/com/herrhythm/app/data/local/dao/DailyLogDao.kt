package com.herrhythm.app.data.local.dao

import androidx.room.*
import com.herrhythm.app.data.local.entity.DailyLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: DailyLogEntity): Long

    @Update
    suspend fun update(log: DailyLogEntity)

    @Delete
    suspend fun delete(log: DailyLogEntity)

    @Query("SELECT * FROM daily_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    suspend fun getLogByDate(date: LocalDate): DailyLogEntity?

    @Query("SELECT * FROM daily_logs WHERE date >= :start AND date <= :end ORDER BY date ASC")
    suspend fun getLogsBetween(start: LocalDate, end: LocalDate): List<DailyLogEntity>

    @Query("SELECT * FROM daily_logs WHERE cycle_id = :cycleId ORDER BY date ASC")
    suspend fun getLogsForCycle(cycleId: Long): List<DailyLogEntity>

    @Query("DELETE FROM daily_logs")
    suspend fun deleteAll()
}
