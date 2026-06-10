package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalDevicesService;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.ReceiveTelemetryReadingCommand;
import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.repositories.StockComparisonTaskRepository;
import com.uitopic.restock.platform.tracking.domain.repositories.TelemetryReadingRepository;
import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the TelemetryReadingCommandService interface that handles the processing of telemetry readings received from devices. This service is responsible for validating the device ID, saving the telemetry reading to the repository, and performing a stock comparison based on the received telemetry data. It interacts with external services to validate device information and retrieve resource data needed for stock comparisons.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryReadingCommandServiceImpl implements TelemetryReadingCommandService {

    // The TelemetryReadingRepository is used to persist telemetry readings received from devices.
    private final TelemetryReadingRepository telemetryReadingRepository;

    // The StockComparisonTaskRepository is used to manage stock comparison tasks that are created based on the telemetry readings. This repository allows for saving and retrieving stock comparison tasks, which can be used for further analysis or reporting.
    private final StockComparisonTaskRepository stockComparisonTaskRepository;

    // The ExternalDevicesService is used to validate the device ID and retrieve device information if needed.
    private final ExternalDevicesService externalDevicesService;

    // The ExternalResourcesService is used to retrieve resource information, such as stock levels, for performing stock comparisons based on telemetry readings.
    private final ExternalResourcesService externalResourcesService;

    @Override
    public void handle(ReceiveTelemetryReadingCommand command) {

        // Validate the device ID before processing the telemetry reading. If the device ID is not recognized, log a warning and throw an exception.
        if (!externalDevicesService.deviceExists(command.deviceId())) {
            log.warn("Received telemetry reading from unknown device: {}", command.deviceId());
            throw new TelemetryValuesException("Device ID " + command.deviceId() + " is not recognized");
        }

        // Create a new TelemetryReading entity based on the received command and save it to the repository. If saving fails, log an error and throw an exception.
        var telemetryReading = new TelemetryReading(command);
        var savedTelemetryReading = telemetryReadingRepository.save(telemetryReading);

        // If the saved telemetry reading is null, it indicates a failure in saving the data. Log an error message and throw a TelemetryValuesException to indicate the failure.
        if (savedTelemetryReading == null) {
            log.error("Failed to save telemetry reading for device: {}", command.deviceId());
            throw new TelemetryValuesException("Failed to save telemetry reading for device ID " + command.deviceId());
        }

        // Perform a stock comparison based on the saved telemetry reading. This involves retrieving the physical stock from the telemetry reading and the system stock from the external resources service, and then comparing the two values to check for any discrepancies. If any exception occurs during this process, it will be handled within the performStockComparison method.
        performStockComparison(command);
    }

    /**
     * Performs a stock comparison based on the telemetry reading received from a device. This method retrieves the physical stock from the telemetry reading and the system stock from the external resources service using the assigned batch ID. It then compares the two stock values and checks for any discrepancies based on a predefined threshold. If an anomaly is detected, it logs a warning message; otherwise, it logs an informational message indicating that no anomaly was detected. If any exception occurs during this process, it logs the error and throws a TelemetryValuesException with details about the failure.
     *
     * @param command the command containing the telemetry reading information, including the physical stock, assigned batch ID, and device ID
     */
    private void performStockComparison(ReceiveTelemetryReadingCommand command) {

        try {
            // Get the physical stock from the telemetry reading and the system stock from the external resources service using the assigned batch ID. Also, retrieve the device ID and the discrepancy threshold for anomaly detection.
            var physicalStock = command.physicalStock();
            var systemStock = new StockRecord(externalResourcesService.getCustomSupplyStockByBatchId(command.assignedBatchId()));
            var deviceId = command.deviceId();
            var discrepancyThreshold = externalDevicesService.getAnomalyThreshold(deviceId);

            // Register a new stock comparison task using the retrieved physical stock, system stock, device ID, and discrepancy threshold. If any exception occurs during this process, log the error and throw a TelemetryValuesException with details about the failure.
            var task = new StockComparisonTask(
                    physicalStock,
                    systemStock,
                    deviceId
            );

            // Check for anomalies by comparing the physical stock and system stock against the discrepancy threshold. If an anomaly is detected, log a warning message with details about the device ID, physical stock, system stock, and the threshold. If no anomaly is detected, log an informational message indicating that the physical stock is within the threshold of the system stock. This allows for monitoring inventory discrepancies and taking appropriate actions if anomalies are detected.
            var isAnomaly = task.isAnomalyDetected(discrepancyThreshold);

            // Evaluate the result of the anomaly detection and log the appropriate message. If an anomaly is detected, it indicates a significant discrepancy between the physical stock and system stock, which may require further investigation or corrective actions. If no anomaly is detected, it indicates that the physical stock is within an acceptable range of the system stock, suggesting that there are no immediate issues with inventory levels for the device.
            if (isAnomaly) {
                task.stockMismatch();
                // TODO: Register an event for when an anomaly is detected, which can be used to trigger alerts or further investigations.
            } else {
                task.stockMatch();
                log.info(
                        "No anomaly detected for device {}: physical stock {} is within the threshold of system stock {}",
                        deviceId.getDeviceId(),
                        physicalStock.getStock(),
                        systemStock.getStock());
            }

            // Save the stock comparison task to the repository. This allows for tracking and analyzing stock comparisons over time, and can be used for reporting or further investigations if anomalies are detected. If saving the task fails, log an error message and throw a TelemetryValuesException to indicate the failure.
            stockComparisonTaskRepository.save(task);

        } catch (Exception e) {
            log.error("Error performing stock comparison for device {}: {}", command.deviceId(), e.getMessage());
            throw new TelemetryValuesException("Error performing stock comparison for device ID " + command.deviceId() + ": " + e.getMessage());
        }
    }
}
