package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.resources.domain.model.events.InventoryWithoutStockEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.EmailContents;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationSourceType;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for handling InventoryWithoutStockEvent. This class listens for InventoryWithoutStockEvent events and processes them by logging the event details and creating a notification using the CommunicationsContextFacade. The notification includes information about the batch, current stock (which is 0), branch name, source type, notification type, event type, and account ID associated with the event.
 */
@Slf4j
@Component
public class InventoryWithoutStockEventHandler {

    private final CommunicationsContextFacade communicationsContextFacade;

    public InventoryWithoutStockEventHandler(CommunicationsContextFacade communicationsContextFacade) {
        this.communicationsContextFacade = communicationsContextFacade;
    }

    @EventListener
    public void on(InventoryWithoutStockEvent event) {
        var eventName = event.getClass().getSimpleName().toUpperCase();
        var inventoryId = String.join(event.getBranchId(), "+", event.getBatchId());

        log.info("{}: Inventory with id {} has no stock.",
                eventName,
                inventoryId
        );

        var contents = EmailContents.builder()
                .resourceName(event.getBatchCode())
                .resourceCount("0")
                .branchName(event.getBranchName())
                .sourceType(NotificationSourceType.CLOUD_SERVER.getValue())
                .notificationType(NotificationType.ALL.getType())
                .eventType(eventName)
                .accountId(event.getAccountId())
                .build();

        communicationsContextFacade.createNotification(contents);
    }
}
