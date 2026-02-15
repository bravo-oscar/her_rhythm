package com.herrhythm.app.notification

import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor() {

    companion object {
        const val DAILY_CHECK_WORK = "daily_check_work"
    }

    fun scheduleDailyCheck(context: Context) {
        val now = LocalDateTime.now()
        var targetTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(9, 0))
        if (now.isAfter(targetTime)) {
            targetTime = targetTime.plusDays(1)
        }
        val delay = Duration.between(now, targetTime).toMillis()

        val request = PeriodicWorkRequestBuilder<DailyCheckWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_CHECK_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun cancelDailyCheck(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(DAILY_CHECK_WORK)
    }
}
