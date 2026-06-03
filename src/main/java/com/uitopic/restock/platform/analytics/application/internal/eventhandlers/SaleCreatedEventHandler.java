package com.uitopic.restock.platform.analytics.application.internal.eventhandlers;

import com.uitopic.restock.platform.analytics.domain.model.events.SaleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class SaleCreatedEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(SaleCreatedEvent event) {

    }
}
