package com.herrhythm.app.presentation.screen.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herrhythm.app.domain.model.CyclePhase
import com.herrhythm.app.domain.model.CycleStatistics
import com.herrhythm.app.presentation.common.components.ConfidenceBadge
import com.herrhythm.app.presentation.common.components.CountdownWidget
import com.herrhythm.app.presentation.common.components.GradientBackground
import com.herrhythm.app.presentation.common.components.PhaseIndicator
import com.herrhythm.app.presentation.common.components.StyledCard
import com.herrhythm.app.presentation.theme.*

@Composable
fun DashboardScreen(
    onNavigateToCycleEntry: () -> Unit,
    onNavigateToPredictions: () -> Unit,
    onNavigateToDailyLog: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadData() }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = onNavigateToDailyLog,
                    containerColor = Lavender,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.EditNote, "Log today")
                }
                FloatingActionButton(
                    onClick = onNavigateToCycleEntry,
                    containerColor = Rose,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(Icons.Default.Add, "New cycle")
                }
            }
        }
    ) { padding ->
        GradientBackground {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (!uiState.hasCycles) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    StyledCard(
                        modifier = Modifier.padding(32.dp),
                        accentColor = Rose
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "\uD83C\uDF38",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Welcome to Her Rhythm",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Start by logging your first cycle. Tap the + button to record when your period started.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(24.dp))
                            Text(
                                text = "Your predictions will improve with each cycle you log. All data stays on your device.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Her Rhythm",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Your cycle, your way",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(24.dp))

                    uiState.prediction?.let { prediction ->
                        PhaseIndicator(
                            phase = prediction.currentPhase,
                            cycleDay = prediction.currentCycleDay
                        )

                        Spacer(Modifier.height(8.dp))
                        ConfidenceBadge(level = prediction.confidenceLevel)

                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CountdownWidget(
                                label = "Next Period",
                                targetDate = prediction.nextPeriodStart,
                                color = MenstrualColor,
                                modifier = Modifier.weight(1f)
                            )
                            CountdownWidget(
                                label = "PMS Window",
                                targetDate = prediction.pmsWindowStart,
                                color = PmsColor,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CountdownWidget(
                                label = "Ovulation",
                                targetDate = prediction.ovulationDate,
                                color = OvulationColor,
                                modifier = Modifier.weight(1f)
                            )
                            CountdownWidget(
                                label = "Fertile Window",
                                targetDate = prediction.fertileWindowStart,
                                color = FertileColor,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = onNavigateToPredictions,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Rose.copy(alpha = 0.12f),
                                contentColor = Rose
                            ),
                            border = BorderStroke(1.dp, Rose.copy(alpha = 0.3f))
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("View Detailed Predictions")
                        }

                        // Cycle Stats Card
                        uiState.cycleStatistics?.let { stats ->
                            Spacer(Modifier.height(24.dp))
                            CycleStatsCard(stats)
                        }

                        // Phase Insights Card
                        if (prediction.currentPhase != CyclePhase.UNKNOWN) {
                            Spacer(Modifier.height(24.dp))
                            PhaseInsightsCard(prediction.currentPhase)
                        }
                    }

                    // Health Resources Section
                    Spacer(Modifier.height(24.dp))
                    HealthResourcesSection()

                    // Bottom spacing for FAB clearance
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun CycleStatsCard(stats: CycleStatistics) {
    StyledCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        accentColor = Lavender
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Lavender.copy(alpha = 0.08f),
                            Rose.copy(alpha = 0.06f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Your Cycle Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatItem("Avg Cycle", "${stats.averageCycleLength} days", Modifier.weight(1f))
                StatItem("Avg Period", "${stats.averagePeriodLength} days", Modifier.weight(1f))
                StatItem("Shortest", "${stats.shortestCycle} days", Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatItem("Longest", "${stats.longestCycle} days", Modifier.weight(1f))
                StatItem("Total Cycles", "${stats.totalCyclesLogged}", Modifier.weight(1f))
                StatItem("Regularity", stats.regularity, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PhaseInsightsCard(phase: CyclePhase) {
    val phaseColor = when (phase) {
        CyclePhase.MENSTRUAL -> MenstrualColor
        CyclePhase.FOLLICULAR -> FollicularColor
        CyclePhase.OVULATION -> OvulationColor
        CyclePhase.LUTEAL -> LutealColor
        CyclePhase.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val emoji = when (phase) {
        CyclePhase.MENSTRUAL -> "\uD83C\uDF38"
        CyclePhase.FOLLICULAR -> "\uD83C\uDF31"
        CyclePhase.OVULATION -> "\u2728"
        CyclePhase.LUTEAL -> "\uD83C\uDF19"
        CyclePhase.UNKNOWN -> ""
    }

    val tip = when (phase) {
        CyclePhase.MENSTRUAL -> "Rest and gentle movement can help. Stay hydrated and prioritize comfort."
        CyclePhase.FOLLICULAR -> "Energy is rising \u2014 great time for planning, socializing, and trying new things."
        CyclePhase.OVULATION -> "Peak energy and confidence. You may feel more social and creative."
        CyclePhase.LUTEAL -> "Winding down \u2014 prioritize sleep, reduce stress, and practice self-care."
        CyclePhase.UNKNOWN -> ""
    }

    StyledCard(
        modifier = Modifier.fillMaxWidth(),
        accentColor = phaseColor,
        containerColor = phaseColor.copy(alpha = 0.08f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "$emoji Phase Insights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = phase.displayName + " Phase",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = phaseColor
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = tip,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private data class ResourceLink(val title: String, val url: String)

private data class ResourceCategory(
    val name: String,
    val links: List<ResourceLink>
)

private val healthResourceCategories = listOf(
    ResourceCategory(
        name = "Understanding Your Cycle",
        links = listOf(
            ResourceLink("ACOG \u2013 Menstrual Cycle Overview", "https://www.acog.org/womens-health/faqs/your-changing-body-puberty-in-girls"),
            ResourceLink("Office on Women's Health \u2013 Menstrual Cycle", "https://www.womenshealth.gov/menstrual-cycle")
        )
    ),
    ResourceCategory(
        name = "Ovulation & Fertility",
        links = listOf(
            ResourceLink("Mayo Clinic \u2013 Getting Pregnant", "https://www.mayoclinic.org/healthy-lifestyle/getting-pregnant/in-depth/how-to-get-pregnant/art-20047611"),
            ResourceLink("Planned Parenthood \u2013 Fertility Awareness", "https://www.plannedparenthood.org/learn/birth-control/fertility-awareness")
        )
    ),
    ResourceCategory(
        name = "PMS & Mood",
        links = listOf(
            ResourceLink("ACOG \u2013 Premenstrual Syndrome", "https://www.acog.org/womens-health/faqs/premenstrual-syndrome"),
            ResourceLink("Office on Women's Health \u2013 PMS", "https://www.womenshealth.gov/menstrual-cycle/premenstrual-syndrome")
        )
    ),
    ResourceCategory(
        name = "Perimenopause & Menopause",
        links = listOf(
            ResourceLink("Mayo Clinic \u2013 Perimenopause", "https://www.mayoclinic.org/diseases-conditions/perimenopause/symptoms-causes/syc-20354666"),
            ResourceLink("NAMS \u2013 Menopause Information", "https://www.menopause.org/for-women")
        )
    ),
    ResourceCategory(
        name = "When to See a Doctor",
        links = listOf(
            ResourceLink("ACOG \u2013 Warning Signs", "https://www.acog.org/womens-health/faqs/abnormal-uterine-bleeding"),
            ResourceLink("Mayo Clinic \u2013 Menstrual Irregularities", "https://www.mayoclinic.org/symptoms/menstrual-cramps/basics/when-to-see-doctor/sym-20050648")
        )
    )
)

@Composable
private fun HealthResourcesSection() {
    Text(
        text = "Health Resources",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(12.dp))

    healthResourceCategories.forEach { category ->
        ExpandableResourceCard(category)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ExpandableResourceCard(category: ResourceCategory) {
    var expanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    StyledCard(
        modifier = Modifier.fillMaxWidth(),
        accentColor = Teal,
        containerColor = Teal.copy(alpha = 0.06f)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 8.dp, end = 16.dp, bottom = 8.dp)) {
                    category.links.forEach { link ->
                        TextButton(
                            onClick = { uriHandler.openUri(link.url) }
                        ) {
                            Text(
                                text = link.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
