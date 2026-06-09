package com.uitopic.restock.platform.tracking.application.internal.eventhandlers;

import com.uitopic.restock.platform.tracking.domain.model.events.ConciliationTaskResolvedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ConciliationTaskResolvedEventHandler {

    @EventListener
    public void on(ConciliationTaskResolvedEvent event) {

    }
}
