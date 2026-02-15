package com.herrhythm.app.domain.model

import java.time.LocalDate

data class Cycle(
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val cycleLength: Int? = null,
    val periodLength: Int? = null,
    val flowIntensity: FlowIntensity = FlowIntensity.MEDIUM,
    val notes: String = "",
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)
