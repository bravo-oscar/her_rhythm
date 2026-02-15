# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew installDebug           # Build and install on connected device
./gradlew test                   # Run all unit tests
./gradlew test --tests "*.PredictionEngineTest"  # Run single test class
./gradlew kspDebugKotlin         # Run KSP code generation (Room, Hilt)
```

## Architecture

Clean Architecture with three layers, all wired via **Hilt DI**:

- **`data/`** — Room database (v2), DAOs, repository implementations, type converters
- **`domain/`** — Models, repository interfaces, use cases (organized by feature: cycle, prediction, log, settings, backup)
- **`presentation/`** — Jetpack Compose screens, ViewModels, navigation, theme, reusable components

Data flows: `Room DB → DAO (Flow) → RepositoryImpl → UseCase → ViewModel → Composable (collectAsStateWithLifecycle)`

## Key Technical Details

- **Single-activity app** — `MainActivity` with Compose Navigation (`NavHost` + bottom nav)
- **No internet permission** — all data is local-only (Room), privacy-by-design
- **Theme system** — supports light/dark/system modes via `UserSettings.themeMode`; detection uses `MaterialTheme.colorScheme.background.luminance()` (not `isSystemInDarkTheme()`) to respect the app's own setting
- **Edge-to-edge** — `enableEdgeToEdge()` with explicit `SystemBarStyle` per theme mode
- **Background work** — `WorkManager` with `HiltWorkerFactory` for daily notification checks
- **Prediction engine** — `PredictionEngine` uses weighted cycle averaging with recency bias; confidence scoring from 0.0-0.95 based on cycle count and regularity
- **KSP** (not KAPT) for Room and Hilt code generation
- **Kotlin 2.1.0**, **Compose BOM 2024.12.01**, **Material3 1.3.1**, **compileSdk 35**, **minSdk 26**, **Java 17**

## UI Conventions

- All screens wrap content in `GradientBackground` (or `GradientBackground(variant = PEACH)` for entry screens)
- Cards use the shared `StyledCard` component (RoundedCornerShape(20.dp), no elevation, transparent border by default)
- Phase colors defined in `Color.kt`: `MenstrualColor`, `FollicularColor`, `OvulationColor`, `LutealColor`, `FertileColor`, `PmsColor`
- Accent palette: `Rose`, `Lavender`, `Teal`, `Peach` — used consistently across chips, FABs, buttons
- Chip groups (Flow, Mood, Symptom, Lifestyle) share pill-shaped styling with accent-specific colors
- Scaffold uses `containerColor = Color.Transparent` to let gradient backgrounds show through

## Navigation Routes

Defined as sealed class `Screen` in `presentation/navigation/Screen.kt`. Bottom nav tabs: Dashboard, Calendar, Logbook, Settings. Additional routes: CycleEntry, DailyLogEntry, Predictions.

## Database

Room database `HerRhythmDatabase` with three entities: `CycleEntity`, `DailyLogEntity`, `UserSettingsEntity`. Schema version 2 with migration support. Room schemas exported to `app/schemas/`.
