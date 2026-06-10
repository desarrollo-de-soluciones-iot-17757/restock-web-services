package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;

/**
 * Value object representing the humidity percentage of a device, used for tracking inventory discrepancies.
 *
 * @param humidityPercentage the humidity percentage, provided by the request
 */
public record HumidityRecord(
        Double humidityPercentage
) {

    /**
     * Creates a humidity record value object.
     *
     * @param humidityPercentage the humidity percentage, provided by the request
     */
    public HumidityRecord {
        if (humidityPercentage == null || humidityPercentage < 0.0 || humidityPercentage > 100.0) {
            throw new TelemetryValuesException("Humidity percentage must be between 0 and 100");
        }
    }

    /**
     * Returns the humidity percentage.
     *
     * @return the humidity percentage
     */
    public Double getHumidityPercentage() {
        return humidityPercentage;
    }
}
