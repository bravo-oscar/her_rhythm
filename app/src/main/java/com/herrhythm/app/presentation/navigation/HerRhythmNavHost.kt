package com.herrhythm.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.herrhythm.app.presentation.screen.calendar.CalendarScreen
import com.herrhythm.app.presentation.screen.dashboard.DashboardScreen
import com.herrhythm.app.presentation.screen.logbook.LogbookScreen
import com.herrhythm.app.presentation.screen.logentry.CycleEntryScreen
import com.herrhythm.app.presentation.screen.logentry.DailyLogEntryScreen
import com.herrhythm.app.presentation.screen.predictions.PredictionsScreen
import com.herrhythm.app.presentation.screen.settings.SettingsScreen

@Composable
fun HerRhythmNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToCycleEntry = { navController.navigate(Screen.CycleEntry.createRoute()) },
                    onNavigateToPredictions = { navController.navigate(Screen.Predictions.route) },
                    onNavigateToDailyLog = { navController.navigate(Screen.DailyLogEntry.createRoute()) }
                )
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    onDayClick = { date ->
                        navController.navigate(Screen.DailyLogEntry.createRoute(date.toString()))
                    }
                )
            }
            composable(Screen.Logbook.route) {
                LogbookScreen(
                    onAddEntry = { navController.navigate(Screen.DailyLogEntry.createRoute()) },
                    onEditEntry = { date ->
                        navController.navigate(Screen.DailyLogEntry.createRoute(date.toString()))
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.CycleEntry.route,
                arguments = listOf(navArgument("cycleId") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { backStackEntry ->
                val cycleId = backStackEntry.arguments?.getLong("cycleId") ?: -1L
                CycleEntryScreen(
                    cycleId = if (cycleId == -1L) null else cycleId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.DailyLogEntry.route,
                arguments = listOf(navArgument("date") {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) { backStackEntry ->
                val dateStr = backStackEntry.arguments?.getString("date") ?: ""
                DailyLogEntryScreen(
                    dateString = dateStr,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Predictions.route) {
                PredictionsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
