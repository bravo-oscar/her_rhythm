package com.herrhythm.app.domain.model

import java.time.LocalDate

data class DailyLog(
    val id: Long = 0,
    val date: LocalDate,
    val cycleId: Long? = null,
    val note: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val symptoms: List<Symptom> = emptyList(),
    val flowIntensity: FlowIntensity = FlowIntensity.NONE,
    val temperature: Float? = null,
    val lifestyleFactors: List<LifestyleFactor> = emptyList(),
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)
