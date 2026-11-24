# ISO2-2026-GrupoA.01-Testing-P3

Step 1 (implementation, Pair 3) – Problem 3: greenhouse irrigation recommendations.
Testing problem 3: Alba & Rafael

## Run

```bash
mvn package
java -cp target/classes iso2.irrigation.Main
```

Inputs: soil moisture (%), temperature (C), rain forecast in next 24h (y/n), time (HH:mm).

## Structure (for the testing pairs)

| Class | Role |
|-------|------|
| `domain.IrrigationAdvisor` | Business rules. Method under test: `IrrigationAction recommend(SensorReading)` |
| `domain.SensorReading` | Immutable input. Constructor validates: moisture 0..100, temperature -50..60, time not null (`IllegalArgumentException`) |
| `domain.IrrigationAction` | Enum with the possible results |
| `ui.ConsoleApp` | Console I/O only (reader/stream injected, testable). No logic |
| `Main` | Entry point |

Tests go in `src/test/java/iso2/irrigation` (JUnit 5). Surefire and JaCoCo are already configured: `mvn test` generates `target/site/jacoco/index.html`.

## Assumptions (ambiguities in the statement)

- "Imminent rain" = "rain forecast in next 24h" (one boolean).
- "below/under" is strict (`<`), "exceeds/above" is strict (`>`), "between 20% and 40%" includes both limits.
- Peak hours are 12:00 to 16:00, both included.
- Rule priority:
  1. Emergency Drainage (moisture > 60 and temperature < 5).
  2. Peak hours: moisture < 10 -> Critical Watering (even with rain forecast); otherwise Heavy/Light Watering becomes "Postpone Watering".
  3. Normal rules: moisture < 40 and rain -> Do Not Water; temperature > 25, no rain and moisture < 20 -> Heavy; 20..40 -> Light.
- If no rule applies: `NO_ACTION`.
