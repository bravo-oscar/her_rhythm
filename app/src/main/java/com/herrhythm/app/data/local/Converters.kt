package com.herrhythm.app.data.local

import androidx.room.TypeConverter
import com.herrhythm.app.domain.model.FlowIntensity
import com.herrhythm.app.domain.model.LifestyleFactor
import com.herrhythm.app.domain.model.Mood
import com.herrhythm.app.domain.model.Symptom
import java.time.LocalDate

class Converters {

    // LocalDate <-> Long (epoch day)

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }

    // FlowIntensity <-> String (enum name)

    @TypeConverter
    fun fromFlowIntensity(flowIntensity: FlowIntensity?): String? {
        return flowIntensity?.name
    }

    @TypeConverter
    fun toFlowIntensity(name: String?): FlowIntensity? {
        return name?.let { FlowIntensity.valueOf(it) }
    }

    // Mood <-> String (enum name)

    @TypeConverter
    fun fromMood(mood: Mood?): String? {
        return mood?.name
    }

    @TypeConverter
    fun toMood(name: String?): Mood? {
        return name?.let { Mood.valueOf(it) }
    }

    // List<Symptom> <-> String (comma-separated names)

    @TypeConverter
    fun fromSymptomList(symptoms: List<Symptom>?): String? {
        return symptoms?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toSymptomList(value: String?): List<Symptom>? {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(",").mapNotNull { name ->
            try { Symptom.valueOf(name.trim()) } catch (e: Exception) { null }
        }
    }

    // List<LifestyleFactor> <-> String (comma-separated names)

    @TypeConverter
    fun fromLifestyleFactorList(factors: List<LifestyleFactor>?): String? {
        return factors?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toLifestyleFactorList(value: String?): List<LifestyleFactor>? {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(",").mapNotNull { name ->
            try { LifestyleFactor.valueOf(name.trim()) } catch (e: Exception) { null }
        }
    }
}
