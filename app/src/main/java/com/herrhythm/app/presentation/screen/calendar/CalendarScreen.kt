package com.herrhythm.app.presentation.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.model.DailyLog
import com.herrhythm.app.domain.model.Prediction
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.StyledCard
import com.herrhythm.app.presentation.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    onDayClick: (LocalDate) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GradientBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Month navigation header
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        viewModel.changeMonth(uiState.currentMonth.minusMonths(1))
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous month")
                    }
                    Text(
                        text = "${uiState.currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${uiState.currentMonth.year}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        viewModel.changeMonth(uiState.currentMonth.plusMonths(1))
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next month")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Day-of-week headers
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Calendar grid
            val month = uiState.currentMonth
            val firstDay = month.atDay(1)
            val firstDayOfWeek = firstDay.dayOfWeek.value % 7
            val daysInMonth = month.lengthOfMonth()
            val logDates = uiState.logs.map { it.date }.toSet()

            var dayCounter = 1
            for (week in 0..5) {
                if (dayCounter > daysInMonth) break
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayOfWeek in 0..6) {
                        if (week == 0 && dayOfWeek < firstDayOfWeek || dayCounter > daysInMonth) {
                            Spacer(Modifier.weight(1f))
                        } else {
                            val date = month.atDay(dayCounter)
                            val dayColor = getDayColor(date, uiState.cycles, uiState.prediction)
                            val hasLog = date in logDates
                            val isToday = date == LocalDate.now()

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .then(
                                        if (dayColor != null) Modifier.background(dayColor.copy(alpha = 0.35f))
                                        else if (isToday) Modifier.border(
                                            width = 2.dp,
                                            brush = Brush.sweepGradient(
                                                colors = listOf(Rose, Peach, Rose)
                                            ),
                                            shape = CircleShape
                                        )
                                        else Modifier
                                    )
                                    .clickable { onDayClick(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = dayCounter.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = dayColor ?: MaterialTheme.colorScheme.onSurface
                                    )
                                    if (hasLog) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                    }
                                }
                            }
                            dayCounter++
                        }
                    }
                }
            }

            // Legend
            Spacer(Modifier.height(24.dp))
            StyledCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem("Period", MenstrualColor)
                    LegendItem("PMS", PmsColor)
                    LegendItem("Fertile", FertileColor)
                    LegendItem("Predicted", PredictedColor)
                }
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.5.dp, color.copy(alpha = 0.5f), CircleShape)
        )
        Spacer(Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

private fun getDayColor(
    date: LocalDate,
    cycles: List<Cycle>,
    prediction: Prediction?
): Color? {
    for (cycle in cycles) {
        val endDate = cycle.endDate ?: cycle.startDate.plusDays((cycle.periodLength ?: 5).toLong() - 1)
        if (!date.isBefore(cycle.startDate) && !date.isAfter(endDate)) {
            return MenstrualColor
        }
    }

    prediction?.let { pred ->
        if (!date.isBefore(pred.nextPeriodStart) && date.isBefore(pred.nextPeriodStart.plusDays(5))) {
            return PredictedColor
        }
        if (!date.isBefore(pred.pmsWindowStart) && date.isBefore(pred.nextPeriodStart)) {
            return PmsColor
        }
        if (!date.isBefore(pred.fertileWindowStart) && !date.isAfter(pred.fertileWindowEnd)) {
            return FertileColor
        }
    }

    return null
}
