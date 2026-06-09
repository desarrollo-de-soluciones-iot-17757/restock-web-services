package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.services.SupplyCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Handles application startup events for the resources bounded context.
 *
 * Seeds the base supply catalog when the application is ready.
 */
@Slf4j
@Service
public class ResourceApplicationReadyEventHandler {

    private final SupplyCommandService supplyCommandService;

    public ResourceApplicationReadyEventHandler(SupplyCommandService supplyCommandService) {
        this.supplyCommandService = supplyCommandService;
    }

    /**
     * Seeds the base supply catalog after the application starts.
     *
     * @param event application ready event
     */
    @EventListener
    public void on(ApplicationReadyEvent event) {
        var appName = event.getApplicationContext().getId();

        log.info("Seeding supplies for {} at {}", appName, timestamp());

        supplyCommandService.handle(new SeedSuppliesCommand());

        log.info("Seeding finished for {} at {}", appName, timestamp());
    }

    private Timestamp timestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}