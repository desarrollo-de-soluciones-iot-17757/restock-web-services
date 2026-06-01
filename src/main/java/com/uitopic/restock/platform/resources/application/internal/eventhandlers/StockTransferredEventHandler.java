package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.StockTransferredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for handling the StockTransferredEvent. This event is triggered when stock is transferred from one inventory to another.
 * The handler logs the details of the transfer, including the origin and destination inventory IDs, the quantity transferred, and the remaining stock in both inventories after the transfer.
 */
@Slf4j
@Component
public class StockTransferredEventHandler {

    @EventListener
    public void on(StockTransferredEvent event) {
        var eventName = event.getClass().getSimpleName().toUpperCase();

        var originInventoryId = String.join(event.getFromBranchId(), "+", event.getBatchId());
        var destinationInventoryId = String.join(event.getToBranchId(), "+", event.getBatchId());

        var transferredQuantity = String.join(event.getQuantityTransferred().toString(), event.getUnitMeasurement());
        var originInventoryRemainingStock = String.join(event.getFromBranchRemainingStock().toString(), event.getUnitMeasurement());
        var destinationInventoryRemainingStock = String.join(event.getToBranchRemainingStock().toString(), event.getUnitMeasurement());

        log.info("{}: Inventory with id {} transferred {} of stock to inventory with id {}.",
                eventName,
                originInventoryId,
                transferredQuantity,
                destinationInventoryId
        );

        log.info("{}: Remaining stock of origin inventory is {} and of destination inventory is {}.",
                eventName,
                originInventoryRemainingStock,
                destinationInventoryRemainingStock
        );
    }
}
