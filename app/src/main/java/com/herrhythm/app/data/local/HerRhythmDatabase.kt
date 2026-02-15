package com.herrhythm.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.herrhythm.app.data.local.dao.CycleDao
import com.herrhythm.app.data.local.dao.DailyLogDao
import com.herrhythm.app.data.local.dao.UserSettingsDao
import com.herrhythm.app.data.local.entity.CycleEntity
import com.herrhythm.app.data.local.entity.DailyLogEntity
import com.herrhythm.app.data.local.entity.UserSettingsEntity

@Database(
    entities = [
        CycleEntity::class,
        DailyLogEntity::class,
        UserSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HerRhythmDatabase : RoomDatabase() {

    abstract fun cycleDao(): CycleDao

    abstract fun dailyLogDao(): DailyLogDao

    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        const val DATABASE_NAME = "her_rhythm_database"
    }
}
