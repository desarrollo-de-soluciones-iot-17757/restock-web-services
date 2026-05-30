package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.StockSubtractedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockSubtractedEventHandler {

    @EventListener
    public void on(StockSubtractedEvent event) {
        log.info("Stock subtracted: deductionId={}, branchId={}, supplyId={}, qty={}, remaining={}",
                event.deductionId(), event.branchId(), event.supplyId(), event.quantity(), event.remainingStock());
    }
}
