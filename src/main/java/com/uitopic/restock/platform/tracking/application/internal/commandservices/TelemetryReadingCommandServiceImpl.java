package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring.SpringDomainEventPublisher;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalDevicesService;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.ClosePendingConciliationTasksCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.ReceiveTelemetryReadingCommand;
import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.domain.model.events.DiscrepancyDetectedEvent;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.repositories.StockComparisonTaskRepository;
import com.uitopic.restock.platform.tracking.domain.repositories.TelemetryReadingRepository;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskCommandService;
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

    // The SpringDomainEventPublisher is used to publish domain events related to telemetry readings and stock comparisons, allowing for decoupled communication between different parts of the application and enabling event-driven architecture patterns.
    private final SpringDomainEventPublisher domainEventPublisher;
    private final ConciliationTaskCommandService conciliationTaskCommandService;

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
     * Performs a stock comparison based on the telemetry reading received from a device. This method retrieves the device physical stock from the telemetry reading, the justified withdrawn stock from devices, and the digital stock from resources. It then compares the digital stock against the total physical stock to detect discrepancies. If any exception occurs during this process, it logs the error and throws a TelemetryValuesException with details about the failure.
     *
     * @param command the command containing the telemetry reading information, including the physical stock, assigned batch ID, and device ID
     */
    private void performStockComparison(ReceiveTelemetryReadingCommand command) {

        try {
            // Get the device physical stock, digital stock snapshot, and justified withdrawn stock needed for the comparison.
            var physicalStock = command.physicalStock();
            var resourceSnapshot = externalResourcesService.getStockSnapshotByBatchId(command.assignedBatchId());
            var systemStock = new StockRecord(resourceSnapshot.stock());
            var customSupplyName = resourceSnapshot.customSupplyName();
            var deviceId = command.deviceId();
            var justifiedWithdrawnStockAndAccount = externalDevicesService.getJustifiedWithdrawnStock(deviceId);
            var justifiedWithdrawnStock = justifiedWithdrawnStockAndAccount.getLeft();
            var accountId = justifiedWithdrawnStockAndAccount.getRight();

            // Register a new stock comparison task using the retrieved physical stock, system stock, device ID, and justified withdrawn stock.
            var task = new StockComparisonTask(
                    physicalStock,
                    systemStock,
                    deviceId,
                    justifiedWithdrawnStock,
                    accountId,
                    new com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId(resourceSnapshot.branchId()),
                    command.assignedBatchId(),
                    resourceSnapshot.customSupplyId(),
                    customSupplyName
            );

            // Check for anomalies by comparing digital stock against total physical stock.
            var isAnomaly = task.isAnomalyDetected();

            // Evaluate the result of the anomaly detection and log the appropriate message. If an anomaly is detected, it indicates a significant discrepancy between the physical stock and system stock, which may require further investigation or corrective actions. If no anomaly is detected, it indicates that the physical stock is within an acceptable range of the system stock, suggesting that there are no immediate issues with inventory levels for the device.
            if (isAnomaly) {
                task.stockMismatch();
                var savedTask = stockComparisonTaskRepository.save(task);
                var riskLevel = DiscrepancyAlertLevel.from(savedTask.getDifference());
                var event = DiscrepancyDetectedEvent.builder()
                        .customSupplyName(customSupplyName)
                        .physicalStock(physicalStock.getStock())
                        .systemStock(systemStock.getStock())
                        .deviceId(deviceId)
                        .accountId(accountId)
                        .alertLevel(riskLevel)
                        .stockComparisonTask(savedTask)
                        .build();
                domainEventPublisher.publish(event);
            } else {
                task.stockMatch();
                var savedTask = stockComparisonTaskRepository.save(task);
                conciliationTaskCommandService.handle(new ClosePendingConciliationTasksCommand(savedTask));
                log.info(
                        "No anomaly detected for device {}: total physical stock {} matches system stock {}",
                        deviceId.getDeviceId(),
                        savedTask.getTotalPhysicalStock(),
                        systemStock.getStock());
            }

        } catch (Exception e) {
            log.error("Error performing stock comparison for device {}: {}", command.deviceId(), e.getMessage());
            throw new TelemetryValuesException("Error performing stock comparison for device ID " + command.deviceId() + ": " + e.getMessage());
        }
    }
}
