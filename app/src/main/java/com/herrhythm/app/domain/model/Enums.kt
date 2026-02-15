package com.herrhythm.app.domain.model

enum class FlowIntensity(val displayName: String) {
    NONE("None"),
    LIGHT("Light"),
    MEDIUM("Medium"),
    HEAVY("Heavy"),
    VERY_HEAVY("Very Heavy")
}

enum class Mood(val displayName: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    CALM("Calm", "😌"),
    ENERGETIC("Energetic", "⚡"),
    SAD("Sad", "😢"),
    ANXIOUS("Anxious", "😰"),
    IRRITABLE("Irritated", "😤"),
    TIRED("Low Energy", "😴"),
    NEUTRAL("Neutral", "😐"),
    FRISKY("Frisky", "😏"),
    SENSITIVE("Sensitive", "🥺"),
    CONFIDENT("Confident", "💪"),
    GRATEFUL("Grateful", "🙏"),
    OVERWHELMED("Overwhelmed", "😵")
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
    HOT_FLASHES("Hot Flashes"),
    DIZZINESS("Dizziness"),
    CONSTIPATION("Constipation"),
    DIARRHEA("Diarrhea"),
    JOINT_PAIN("Joint Pain"),
    ABDOMINAL_PAIN("Abdominal Pain"),
    NIGHT_SWEATS("Night Sweats")
}

enum class LifestyleFactor(val displayName: String, val emoji: String) {
    TRAVEL("Travel", "✈️"),
    STRESS("Stress", "😓"),
    ALCOHOL("Alcohol", "🍷"),
    POOR_SLEEP("Poor Sleep", "😪"),
    EXERCISE("Exercise", "🏃"),
    SICK("Sick", "🤒"),
    MEDICATION("Medication", "💊"),
    CAFFEINE("Caffeine", "☕"),
    SOCIAL_EVENT("Social Event", "🎉"),
    WORK_PRESSURE("Work Pressure", "💼")
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
