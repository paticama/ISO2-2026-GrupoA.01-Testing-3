package iso2.irrigation.domain;

/** Possible recommendations produced by the irrigation system. */
public enum IrrigationAction {
    HEAVY_WATERING("Heavy Watering"),
    LIGHT_WATERING("Light Watering"),
    DO_NOT_WATER("Do Not Water"),
    EMERGENCY_DRAINAGE("Emergency Drainage"),
    CRITICAL_WATERING("Critical Watering"),
    POSTPONE_WATERING("Postpone Watering (peak solar radiation)"),
    NO_ACTION("No action needed");

    private final String description;

    IrrigationAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
