package com.herrhythm.app.domain.model

data class CycleStatistics(
    val averageCycleLength: Int,
    val averagePeriodLength: Int,
    val shortestCycle: Int,
    val longestCycle: Int,
    val totalCyclesLogged: Int,
    val regularity: String
)
