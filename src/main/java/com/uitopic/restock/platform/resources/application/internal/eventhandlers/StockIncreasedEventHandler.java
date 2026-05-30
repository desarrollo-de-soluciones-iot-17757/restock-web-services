package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.StockIncreasedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockIncreasedEventHandler {

    @EventListener
    public void on(StockIncreasedEvent event) {
        log.info("Stock increased: batchId={}, branchId={}, supplyId={}, qty={}",
                event.batchId(), event.branchId(), event.supplyId(), event.quantity());
    }
}
