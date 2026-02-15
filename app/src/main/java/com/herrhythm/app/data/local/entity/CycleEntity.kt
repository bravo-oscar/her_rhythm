package com.herrhythm.app.data.local.entity

import androidx.room.*
import com.herrhythm.app.domain.model.FlowIntensity
import java.time.LocalDate

@Entity(tableName = "cycles")
data class CycleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "start_date") val startDate: LocalDate,
    @ColumnInfo(name = "end_date") val endDate: LocalDate? = null,
    @ColumnInfo(name = "cycle_length") val cycleLength: Int? = null,
    @ColumnInfo(name = "period_length") val periodLength: Int? = null,
    @ColumnInfo(name = "flow_intensity") val flowIntensity: FlowIntensity = FlowIntensity.MEDIUM,
    val notes: String = "",
    @ColumnInfo(name = "created_at") val createdAt: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDate = LocalDate.now()
)
