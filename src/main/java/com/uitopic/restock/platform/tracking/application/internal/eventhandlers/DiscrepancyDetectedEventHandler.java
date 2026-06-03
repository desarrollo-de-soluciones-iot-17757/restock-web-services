package com.uitopic.restock.platform.tracking.application.internal.eventhandlers;

import com.uitopic.restock.platform.tracking.domain.model.events.DiscrepancyDetectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DiscrepancyDetectedEventHandler {

    @EventListener
    public void on(DiscrepancyDetectedEvent event) {

    }
}
