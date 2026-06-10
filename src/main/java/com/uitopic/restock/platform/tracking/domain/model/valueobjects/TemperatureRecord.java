package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;

/**
 * Value object representing the temperature reading of a device, used for tracking inventory discrepancies.
 *
 * @param temperatureInCelsius the temperature in Celsius, provided by the request
 */
public record TemperatureRecord(
        Double temperatureInCelsius
) {

    private static final Double MIN_TEMPERATURE = -273.15; // Absolute zero in Celsius
    private static final Double MAX_TEMPERATURE = 56.7; // Highest recorded temperature

    /**
     * Creates a temperature record value object.
     *
     * @param temperatureInCelsius the temperature in Celsius, provided by the request
     */
    public TemperatureRecord {
        if (temperatureInCelsius == null || temperatureInCelsius < MIN_TEMPERATURE || temperatureInCelsius > MAX_TEMPERATURE) {
            throw new TelemetryValuesException("Temperature must be between -273.15 and 56.7 degrees Celsius");
        }
    }

    /**
     * Returns the temperature in Celsius.
     *
     * @return the temperature in Celsius
     */
    public Double getTemperatureInCelsius() {
        return temperatureInCelsius;
    }
}
