package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.StockTransferredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Handles stock transfer events.
 *
 * This handler only logs the transfer for now. No inventory synchronization is
 * performed because Inventory is not part of the current flow.
 */
@Slf4j
@Component
public class StockTransferredEventHandler {

    /**
     * Logs the stock transfer event.
     *
     * @param event stock transferred event
     */
    @EventListener
    public void on(StockTransferredEvent event) {
        log.info(
                "Stock transferred: sourceBatchId={}, targetBatchId={}, quantity={}",
                event.getFromBatchId(),
                event.getToBranchId(),
                event.getQuantityTransferred()
        );
    }
}