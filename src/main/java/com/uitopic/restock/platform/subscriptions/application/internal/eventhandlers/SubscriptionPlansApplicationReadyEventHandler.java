package com.uitopic.restock.platform.subscriptions.application.internal.eventhandlers;

import com.uitopic.restock.platform.subscriptions.domain.model.commands.SeedSubscriptionPlansCommand;
import com.uitopic.restock.platform.subscriptions.domain.services.PlanCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscriptionPlansApplicationReadyEventHandler {
    private final PlanCommandService planCommandService;

    public SubscriptionPlansApplicationReadyEventHandler(PlanCommandService planCommandService) {
        this.planCommandService = planCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        log.info("Seeding subscription plans for {}", event.getApplicationContext().getId());
        planCommandService.handle(new SeedSubscriptionPlansCommand());
    }
}
