package com.herrhythm.app.data.local.entity

import androidx.room.*
import com.herrhythm.app.domain.model.FlowIntensity
import com.herrhythm.app.domain.model.Mood
import com.herrhythm.app.domain.model.Symptom
import java.time.LocalDate

@Entity(
    tableName = "daily_logs",
    indices = [Index(value = ["date"], unique = true)]
)
data class DailyLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    @ColumnInfo(name = "cycle_id") val cycleId: Long? = null,
    val note: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val symptoms: List<Symptom> = emptyList(),
    @ColumnInfo(name = "flow_intensity") val flowIntensity: FlowIntensity = FlowIntensity.NONE,
    val temperature: Float? = null,
    @ColumnInfo(name = "created_at") val createdAt: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDate = LocalDate.now()
)
