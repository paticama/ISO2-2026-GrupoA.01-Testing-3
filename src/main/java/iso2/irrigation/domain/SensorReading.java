package iso2.irrigation.domain;

import java.time.LocalTime;

/**
 * Immutable set of input data for one recommendation.
 * Validates its values in the constructor (invalid data -> IllegalArgumentException).
 */
public class SensorReading {

    public static final double MIN_MOISTURE = 0;      // %
    public static final double MAX_MOISTURE = 100;    // %
    public static final double MIN_TEMPERATURE = -50; // degrees C
    public static final double MAX_TEMPERATURE = 60;  // degrees C

    private final double soilMoisture;
    private final double temperature;
    private final boolean rainForecast;
    private final LocalTime time;

    /**
     * @param soilMoisture soil moisture in % (0..100)
     * @param temperature  ambient temperature in degrees C (-50..60)
     * @param rainForecast true if rain is forecast within the next 24 hours
     * @param time         current time of day (not null)
     * @throws IllegalArgumentException if any value is out of range or null
     */
    public SensorReading(double soilMoisture, double temperature, boolean rainForecast, LocalTime time) {
        if (Double.isNaN(soilMoisture) || soilMoisture < MIN_MOISTURE || soilMoisture > MAX_MOISTURE) {
            throw new IllegalArgumentException("Soil moisture must be between 0 and 100 (%)");
        }
        if (Double.isNaN(temperature) || temperature < MIN_TEMPERATURE || temperature > MAX_TEMPERATURE) {
            throw new IllegalArgumentException("Temperature must be between -50 and 60 (C)");
        }
        if (time == null) {
            throw new IllegalArgumentException("Time must not be null");
        }
        this.soilMoisture = soilMoisture;
        this.temperature = temperature;
        this.rainForecast = rainForecast;
        this.time = time;
    }

    public double getSoilMoisture() { return soilMoisture; }
    public double getTemperature() { return temperature; }
    public boolean isRainForecast() { return rainForecast; }
    public LocalTime getTime() { return time; }
}
