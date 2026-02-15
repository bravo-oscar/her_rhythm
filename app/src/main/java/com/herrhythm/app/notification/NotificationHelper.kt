package com.herrhythm.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.herrhythm.app.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor() {

    companion object {
        const val CHANNEL_PREDICTIONS = "cycle_predictions"
        const val CHANNEL_REMINDERS = "daily_reminders"
        const val NOTIFICATION_ID_PERIOD = 1001
        const val NOTIFICATION_ID_PMS = 1002
        const val NOTIFICATION_ID_REMINDER = 1003
    }

    fun createChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val predictionsChannel = NotificationChannel(
            CHANNEL_PREDICTIONS,
            "Cycle Predictions",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications about upcoming cycle events"
        }

        val remindersChannel = NotificationChannel(
            CHANNEL_REMINDERS,
            "Daily Reminders",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Daily log reminders"
        }

        manager.createNotificationChannels(listOf(predictionsChannel, remindersChannel))
    }

    fun showPeriodNotification(context: Context, daysUntil: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val text = if (daysUntil == 0) "Your period is predicted to start today."
                   else "Your period is predicted to start in $daysUntil day${if (daysUntil > 1) "s" else ""}."

        val notification = NotificationCompat.Builder(context, CHANNEL_PREDICTIONS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Period Reminder")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICATION_ID_PERIOD, notification)
    }

    fun showPmsNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_PREDICTIONS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("PMS Window")
            .setContentText("Your PMS window may be starting today.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICATION_ID_PMS, notification)
    }

    fun showDailyReminder(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Daily Log Reminder")
            .setContentText("Don't forget to log how you're feeling today.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICATION_ID_REMINDER, notification)
    }
}
