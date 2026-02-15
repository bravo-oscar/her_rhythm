package com.herrhythm.app.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.herrhythm.app.domain.usecase.prediction.GetPredictionUseCase
import com.herrhythm.app.domain.usecase.settings.GetSettingsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@HiltWorker
class DailyCheckWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getPredictionUseCase: GetPredictionUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val settings = getSettingsUseCase().first()
            val prediction = getPredictionUseCase()
            val today = LocalDate.now()

            // Check period notification
            if (settings.notifyBeforePeriod) {
                val daysUntilPeriod = ChronoUnit.DAYS.between(today, prediction.nextPeriodStart).toInt()
                if (daysUntilPeriod in 0..settings.notifyBeforePeriodDays) {
                    notificationHelper.showPeriodNotification(context, daysUntilPeriod)
                }
            }

            // Check PMS notification
            if (settings.notifyPmsOnset) {
                if (today == prediction.pmsWindowStart) {
                    notificationHelper.showPmsNotification(context)
                }
            }

            // Daily reminder
            if (settings.dailyReminderEnabled) {
                notificationHelper.showDailyReminder(context)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
