package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.StockSubtractedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for handling the StockSubtractedEvent. This event is triggered when stock is subtracted from an inventory due to reasons such as sales, damage, theft, or expiration.
 * The handler logs the details of the stock subtraction, including the inventory ID, the quantity subtracted, and the remaining stock after the subtraction.
 */
@Slf4j
@Component
public class StockSubtractedEventHandler {

    @EventListener
    public void on(StockSubtractedEvent event) {
        var eventName = event.getClass().getSimpleName().toUpperCase();
        var inventoryId = String.join(event.getBranchId(), "+", event.getBatchId());
        var quantitySubtracted = String.join(event.getQuantitySubtracted().toString(), event.getUnitMeasurement());
        var remainingStock = String.join(event.getRemainingStock().toString(), event.getUnitMeasurement());

        log.info("{}: Inventory with id {} had {} of stock subtracted. Remaining stock is {}.",
                eventName,
                inventoryId,
                quantitySubtracted,
                remainingStock
        );
    }
}
