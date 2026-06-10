package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.commands.ReceiveTelemetryReadingCommand;

/**
 * Service interface for handling commands related to telemetry readings, specifically the command to receive a telemetry reading from a device. This service is responsible for processing the incoming command, validating the data, and creating a new telemetry reading entity based on the information provided in the command. The service ensures that the telemetry reading is stored correctly in the system for future reference and analysis, allowing for effective tracking of inventory discrepancies and monitoring of environmental conditions.
 */
public interface TelemetryReadingCommandService {

    /**
     * Handles the command to receive a telemetry reading, processing the data contained in the command to create a new telemetry reading entity and persist it in the system. This method is responsible for validating the command, extracting the relevant information, and ensuring that the telemetry reading is stored correctly for future reference and analysis.
     *
     * @param command the command containing the data for the telemetry reading, including physical stock, temperature, humidity, and device ID, provided by the request
     */
    void handle(ReceiveTelemetryReadingCommand command);
}
