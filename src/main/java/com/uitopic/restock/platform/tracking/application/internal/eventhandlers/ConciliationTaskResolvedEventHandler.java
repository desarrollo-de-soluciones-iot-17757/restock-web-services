package com.uitopic.restock.platform.tracking.application.internal.eventhandlers;

import com.uitopic.restock.platform.tracking.domain.model.events.ConciliationTaskResolvedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ConciliationTaskResolvedEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ConciliationTaskResolvedEvent event) {

    }
}
