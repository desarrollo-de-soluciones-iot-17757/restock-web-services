package com.uitopic.restock.platform.tracking.application.internal.eventhandlers;

import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalCommunicationsService;
import com.uitopic.restock.platform.tracking.domain.model.commands.RegisterDiscrepancyCommand;
import com.uitopic.restock.platform.tracking.domain.model.events.DiscrepancyDetectedEvent;
import com.uitopic.restock.platform.tracking.domain.services.DiscrepancyCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for handling DiscrepancyDetectedEvent events. This handler is responsible for processing the detected discrepancy by logging the details of the anomaly, registering the discrepancy using the DiscrepancyCommandService, and creating a notification to inform relevant parties about the detected anomaly using the ExternalCommunicationsService. The handler listens for DiscrepancyDetectedEvent events and performs the necessary actions to ensure that the discrepancy is properly recorded and communicated to the appropriate recipients.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscrepancyDetectedEventHandler {

    // The DiscrepancyCommandService is used to register the detected discrepancy in the system. This service allows the handler to create a new discrepancy record based on the details of the detected anomaly, such as the difference between physical and system stock levels and the device ID associated with the discrepancy. By using this service, the handler can ensure that the detected discrepancy is properly recorded in the system for further analysis and resolution.
    private final DiscrepancyCommandService discrepancyCommandService;

    // The ExternalCommunicationsService is used to create notifications for detected discrepancies. This service allows the handler to send notifications to relevant parties, such as warehouse managers or inventory control teams, informing them about the detected anomaly and providing details about the discrepancy. By using this service, the handler can ensure that appropriate actions can be taken to investigate and resolve the issue in a timely manner.
    private final ExternalCommunicationsService externalCommunicationsService;

    @EventListener
    public void on(DiscrepancyDetectedEvent event) {
        log.warn(
                "Anomaly detected for device {}: physical stock {} differs from system stock {} by more than the threshold of {}",
                event.getDeviceId().getDeviceId(),
                event.getPhysicalStock(),
                event.getSystemStock(),
                event.getThresholdUsed()
        );

        // Create a discrepancy object based on the details of the detected anomaly, such as the difference between physical and system stock levels and the device ID associated with the discrepancy. This will allow us to register the discrepancy in the system using the DiscrepancyCommandService, ensuring that it is properly recorded for further analysis and resolution.
        var difference = Math.abs(event.getPhysicalStock() - event.getSystemStock());
        var registerDiscrepancyCommand = new RegisterDiscrepancyCommand(difference, event.getAlertLevel(), event.getDeviceId());

        // Creates a discrepancy object and a conciliation task based on the details of the detected anomaly, such as the difference between physical and system stock levels and the device ID associated with the discrepancy. This will allow us to register the discrepancy in the system using the DiscrepancyCommandService, ensuring that it is properly recorded for further analysis and resolution.
        discrepancyCommandService.handle(registerDiscrepancyCommand);

        // After registering the discrepancy, create a notification to inform relevant parties about the detected anomaly. This will help ensure that appropriate actions can be taken to investigate and resolve the issue in a timely manner.
        externalCommunicationsService.createNotification(event, event.getAccountId());
    }
}
