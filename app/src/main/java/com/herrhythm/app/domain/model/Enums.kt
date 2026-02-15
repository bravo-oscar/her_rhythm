package com.herrhythm.app.domain.model

enum class FlowIntensity(val displayName: String) {
    NONE("None"),
    LIGHT("Light"),
    MEDIUM("Medium"),
    HEAVY("Heavy"),
    VERY_HEAVY("Very Heavy")
}

enum class Mood(val displayName: String) {
    HAPPY("Happy"),
    CALM("Calm"),
    ENERGETIC("Energetic"),
    SAD("Sad"),
    ANXIOUS("Anxious"),
    IRRITABLE("Irritable"),
    TIRED("Tired"),
    NEUTRAL("Neutral")
}

enum class Symptom(val displayName: String) {
    CRAMPS("Cramps"),
    HEADACHE("Headache"),
    BLOATING("Bloating"),
    BREAST_TENDERNESS("Breast Tenderness"),
    BACK_PAIN("Back Pain"),
    NAUSEA("Nausea"),
    FATIGUE("Fatigue"),
    ACNE("Acne"),
    INSOMNIA("Insomnia"),
    CRAVINGS("Cravings"),
    MOOD_SWINGS("Mood Swings"),
    HOT_FLASHES("Hot Flashes")
}

enum class CyclePhase(val displayName: String) {
    MENSTRUAL("Menstrual"),
    FOLLICULAR("Follicular"),
    OVULATION("Ovulation"),
    LUTEAL("Luteal"),
    UNKNOWN("Unknown")
}

enum class ConfidenceLevel(val displayName: String) {
    LOW("Low"),
    MODERATE("Moderate"),
    GOOD("Good"),
    HIGH("High");

    companion object {
        fun fromScore(score: Float): ConfidenceLevel {
            return when {
                score < 0.25f -> LOW
                score < 0.50f -> MODERATE
                score < 0.75f -> GOOD
                else -> HIGH
            }
        }
    }
}
