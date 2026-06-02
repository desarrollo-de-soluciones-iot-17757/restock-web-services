package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.resources.domain.model.events.InventoryBelowMinimumStockEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.EmailContents;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationSourceType;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for handling InventoryBelowMinimumStockEvent. This class listens for InventoryBelowMinimumStockEvent events and processes them by logging the event details and creating a notification using the CommunicationsContextFacade. The notification includes information about the batch, current stock, branch name, source type, notification type, event type, and account ID associated with the event.
 */
@Slf4j
@Component
public class InventoryBelowMinimumStockEventHandler {

    private final CommunicationsContextFacade communicationsContextFacade;

    public InventoryBelowMinimumStockEventHandler(CommunicationsContextFacade communicationsContextFacade) {
        this.communicationsContextFacade = communicationsContextFacade;
    }

    @EventListener
    public void on(InventoryBelowMinimumStockEvent event) {
        var eventName = event.getClass().getSimpleName().toUpperCase();
        var inventoryId = String.join(event.getBranchId(), "+", event.getBatchId());
        var currentStock = String.join(event.getCurrentStock().toString(), event.getUnitMeasurement());
        var minimumStock = String.join(event.getMinimumStock().toString(), event.getUnitMeasurement());

        log.info("{}: Inventory with id {} has {} stock, which is below its minimum. Minimum stock is {}.",
                eventName,
                inventoryId,
                currentStock,
                minimumStock
        );

        var contents = EmailContents.builder()
                .resourceName(event.getBatchCode())
                .resourceCount(event.getCurrentStock().toString())
                .branchName(event.getBranchName())
                .sourceType(NotificationSourceType.CLOUD_SERVER.getValue())
                .notificationType(NotificationType.ALL.getType())
                .eventType(eventName)
                .accountId(event.getAccountId())
                .build();

        communicationsContextFacade.createNotification(contents);
    }
}
