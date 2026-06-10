package com.uitopic.restock.platform.tracking.domain.model.commands;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.HumidityRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TemperatureRecord;

/**
 * Command representing the reception of a telemetry reading from a device, containing physical stock, temperature, humidity, and device ID.
 *
 * @param physicalStock the physical stock level recorded by the device, provided by the request
 * @param temperatureInCelsius the temperature in Celsius recorded by the device, provided by the request
 * @param humidityPercentage the humidity percentage recorded by the device, provided by the request
 * @param deviceId the unique identifier of the device that sent the telemetry reading, provided by the request
 */
public record ReceiveTelemetryReadingCommand(
        StockRecord physicalStock,
        TemperatureRecord temperatureInCelsius,
        HumidityRecord humidityPercentage,
        DeviceId deviceId
) {

    public ReceiveTelemetryReadingCommand {
        if (physicalStock == null) {
            throw new TelemetryValuesException("Physical stock cannot be null");
        }
        if (temperatureInCelsius == null) {
            throw new TelemetryValuesException("Temperature cannot be null");
        }
        if (humidityPercentage == null) {
            throw new TelemetryValuesException("Humidity cannot be null");
        }
        if (deviceId == null) {
            throw new TelemetryValuesException("Device ID cannot be null");
        }
    }
}
