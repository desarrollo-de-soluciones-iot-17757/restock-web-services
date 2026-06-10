package com.uitopic.restock.platform.tracking.interfaces.rest.transform;

import com.uitopic.restock.platform.shared.interfaces.rest.transform.SharedValueObjectFromStringAssembler;
import com.uitopic.restock.platform.tracking.domain.model.commands.ReceiveTelemetryReadingCommand;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.TelemetryReadingResource;

/**
 * Assembler class responsible for converting TelemetryReadingResource objects from REST requests into ReceiveTelemetryReadingCommand objects for processing in the application layer.
 */
public class ReceiveTelemetryReadingCommandFromResourceAssembler {

    /**
     * Converts a TelemetryReadingResource to a ReceiveTelemetryReadingCommand.
     *
     * @param resource the resource containing the telemetry reading data from the REST request
     * @return a ReceiveTelemetryReadingCommand populated with the data from the resource, ready to be processed by the application layer
     */
    public static ReceiveTelemetryReadingCommand toCommandFromResource(TelemetryReadingResource resource) {
        var physicalStock = TrackingValueObjectsAssembler.toStockRecordFromNumber(resource.physicalStock());
        var temperature = TrackingValueObjectsAssembler.toTemperatureRecordFromNumber(resource.temperatureInCelsius());
        var humidity = TrackingValueObjectsAssembler.toHumidityRecordFromNumber(resource.humidityPercentage());
        var assignedBatchId = SharedValueObjectFromStringAssembler.toBatchIdFromString(resource.assignedBatchId());
        var deviceId = SharedValueObjectFromStringAssembler.toDeviceIdFromString(resource.deviceId());

        return new ReceiveTelemetryReadingCommand(
                physicalStock,
                temperature,
                humidity,
                assignedBatchId,
                deviceId
        );
    }
}
