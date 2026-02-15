package com.herrhythm.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Calendar : Screen("calendar")
    data object Logbook : Screen("logbook")
    data object Settings : Screen("settings")
    data object CycleEntry : Screen("cycle_entry?cycleId={cycleId}") {
        fun createRoute(cycleId: Long? = null): String {
            return if (cycleId != null) "cycle_entry?cycleId=$cycleId" else "cycle_entry"
        }
    }
    data object DailyLogEntry : Screen("daily_log_entry?date={date}") {
        fun createRoute(date: String? = null): String {
            return if (date != null) "daily_log_entry?date=$date" else "daily_log_entry"
        }
    }
    data object Predictions : Screen("predictions")
}
