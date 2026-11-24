package iso2.irrigation.domain;

import java.time.LocalTime;

/**
 * Business rules of the greenhouse irrigation system (no I/O here).
 *
 * Assumptions made to remove ambiguities (see README):
 *  - "Imminent rain" and "rain forecast within 24h" are the same boolean.
 *  - Peak hours are 12:00 to 16:00, both ends included.
 *  - All ranges follow the statement literally: "below"/"under" is strict (<),
 *    "exceeds"/"above" is strict (>), "between 20% and 40%" includes both ends.
 *  - Priority of rules: 1) Emergency Drainage, 2) peak-hours rule, 3) normal rules.
 *  - In peak hours: moisture < 10 -> Critical Watering (even if rain is forecast);
 *    otherwise a Heavy/Light watering is replaced by "Postpone Watering".
 *  - If no rule applies the result is NO_ACTION.
 */
public class IrrigationAdvisor {

    static final double CRITICAL_MOISTURE = 10;
    static final double LOW_MOISTURE = 20;
    static final double MEDIUM_MOISTURE = 40;
    static final double HIGH_MOISTURE = 60;
    static final double HOT_TEMPERATURE = 25;
    static final double FREEZING_TEMPERATURE = 5;
    static final LocalTime PEAK_START = LocalTime.of(12, 0);
    static final LocalTime PEAK_END = LocalTime.of(16, 0);

    /** @throws IllegalArgumentException if reading is null */
    public IrrigationAction recommend(SensorReading reading) {
        if (reading == null) {
            throw new IllegalArgumentException("Reading must not be null");
        }
        double moisture = reading.getSoilMoisture();
        double temperature = reading.getTemperature();
        boolean rain = reading.isRainForecast();

        if (moisture > HIGH_MOISTURE && temperature < FREEZING_TEMPERATURE) {
            return IrrigationAction.EMERGENCY_DRAINAGE;
        }

        if (isPeakHour(reading.getTime())) {
            if (moisture < CRITICAL_MOISTURE) {
                return IrrigationAction.CRITICAL_WATERING;
            }
            IrrigationAction standard = standardAction(moisture, temperature, rain);
            if (standard == IrrigationAction.HEAVY_WATERING || standard == IrrigationAction.LIGHT_WATERING) {
                return IrrigationAction.POSTPONE_WATERING;
            }
            return standard;
        }

        return standardAction(moisture, temperature, rain);
    }

    private boolean isPeakHour(LocalTime time) {
        return !time.isBefore(PEAK_START) && !time.isAfter(PEAK_END);
    }

    private IrrigationAction standardAction(double moisture, double temperature, boolean rain) {
        if (moisture < MEDIUM_MOISTURE && rain) {
            return IrrigationAction.DO_NOT_WATER;
        }
        if (temperature > HOT_TEMPERATURE && !rain) {
            if (moisture < LOW_MOISTURE) {
                return IrrigationAction.HEAVY_WATERING;
            }
            if (moisture <= MEDIUM_MOISTURE) {
                return IrrigationAction.LIGHT_WATERING;
            }
        }
        return IrrigationAction.NO_ACTION;
    }
}
