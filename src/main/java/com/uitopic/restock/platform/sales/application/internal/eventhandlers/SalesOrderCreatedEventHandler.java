package com.uitopic.restock.platform.sales.application.internal.eventhandlers;

import com.uitopic.restock.platform.sales.domain.model.events.SalesOrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class SalesOrderCreatedEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(SalesOrderCreatedEvent event) {

    }
}
