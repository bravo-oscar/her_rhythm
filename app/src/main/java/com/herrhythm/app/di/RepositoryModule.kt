package com.herrhythm.app.di

import com.herrhythm.app.data.repository.CycleRepositoryImpl
import com.herrhythm.app.data.repository.DailyLogRepositoryImpl
import com.herrhythm.app.data.repository.UserSettingsRepositoryImpl
import com.herrhythm.app.domain.repository.CycleRepository
import com.herrhythm.app.domain.repository.DailyLogRepository
import com.herrhythm.app.domain.repository.UserSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCycleRepository(impl: CycleRepositoryImpl): CycleRepository

    @Binds
    @Singleton
    abstract fun bindDailyLogRepository(impl: DailyLogRepositoryImpl): DailyLogRepository

    @Binds
    @Singleton
    abstract fun bindUserSettingsRepository(impl: UserSettingsRepositoryImpl): UserSettingsRepository
}
