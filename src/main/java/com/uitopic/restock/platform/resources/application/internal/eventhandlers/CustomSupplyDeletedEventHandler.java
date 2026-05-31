package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for {@link CustomSupplyDeletedEvent} within the resources bounded context.
 */
@Slf4j
@Component
public class CustomSupplyDeletedEventHandler {

    @EventListener
    public void on(CustomSupplyDeletedEvent event) {
        log.info("Custom supply deleted: supplyId={}, accountId={}", event.supplyId(), event.accountId());
    }
}
