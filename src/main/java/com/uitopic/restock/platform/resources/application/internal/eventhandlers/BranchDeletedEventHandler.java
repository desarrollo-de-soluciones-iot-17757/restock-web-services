package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BranchDeletedEventHandler {

    @EventListener
    public void on(BranchDeletedEvent event) {
        log.info("Branch deleted: branchId={}, accountId={}", event.branchId(), event.accountId());
    }
}
