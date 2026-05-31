package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.StockTransferredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for {@link StockTransferredEvent} within the resources bounded context.
 */
@Slf4j
@Component
public class StockTransferredEventHandler {

    @EventListener
    public void on(StockTransferredEvent event) {
        log.info("Stock transferred: transferId={}, from={}, to={}, supplyId={}, qty={}",
                event.transferId(), event.fromBranchId(), event.toBranchId(), event.supplyId(), event.quantity());
    }
}
