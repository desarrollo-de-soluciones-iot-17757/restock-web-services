package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.application.internal.outboundservices.acl.ExternalCommunicationsService;
import com.uitopic.restock.platform.resources.domain.model.events.InventoryBelowMinimumStockEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for InventoryBelowMinimumStockEvent, responsible for processing events when inventory falls below the minimum stock level.
 * This handler logs the event details and triggers a notification through the ExternalCommunicationsService.
 */
@Slf4j
@Component
public class InventoryBelowMinimumStockEventHandler {

    /**
     * ExternalCommunicationsService is used to send notifications to the appropriate channels when inventory falls below the minimum stock level.
     * This service abstracts the communication logic, allowing the event handler to focus on processing the event and delegating the notification task.
     */
    private final ExternalCommunicationsService externalCommunicationsService;

    /**
     * Constructor for InventoryBelowMinimumStockEventHandler, injecting the ExternalCommunicationsService dependency.
     *
     * @param externalCommunicationsService the service used to send notifications
     */
    public InventoryBelowMinimumStockEventHandler(ExternalCommunicationsService externalCommunicationsService) {
        this.externalCommunicationsService = externalCommunicationsService;
    }

    /**
     * Event listener method that handles InventoryBelowMinimumStockEvent. It logs the event details and triggers a notification through the ExternalCommunicationsService.
     *
     * @param event the InventoryBelowMinimumStockEvent containing details about the inventory status
     */
    @EventListener
    public void on(InventoryBelowMinimumStockEvent event) {
        log.info(
                "Inventory stock alert triggered: batchId={}, currentStock={}, minimumStock={}, alertLevel={}",
                event.getBatchId(),
                event.getCurrentStock(),
                event.getMinimumStock(),
                event.getAlertLevel()
        );

        externalCommunicationsService.createNotification(event, new AccountId(event.getAccountId()));
    }
}
