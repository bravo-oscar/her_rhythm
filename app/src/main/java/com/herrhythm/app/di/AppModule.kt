package com.herrhythm.app.di

import android.content.Context
import androidx.room.Room
import com.herrhythm.app.data.local.HerRhythmDatabase
import com.herrhythm.app.data.local.dao.CycleDao
import com.herrhythm.app.data.local.dao.DailyLogDao
import com.herrhythm.app.data.local.dao.UserSettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HerRhythmDatabase {
        return Room.databaseBuilder(
            context,
            HerRhythmDatabase::class.java,
            "her_rhythm_database"
        ).addMigrations(HerRhythmDatabase.MIGRATION_1_2).build()
    }

    @Provides
    fun provideCycleDao(database: HerRhythmDatabase): CycleDao = database.cycleDao()

    @Provides
    fun provideDailyLogDao(database: HerRhythmDatabase): DailyLogDao = database.dailyLogDao()

    @Provides
    fun provideUserSettingsDao(database: HerRhythmDatabase): UserSettingsDao = database.userSettingsDao()
}
