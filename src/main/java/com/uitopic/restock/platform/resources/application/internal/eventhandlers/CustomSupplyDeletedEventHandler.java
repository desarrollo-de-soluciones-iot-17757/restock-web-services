package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles events related to custom supply deletion.
 *
 * When a custom supply is deleted, the batches associated with it are removed.
 * This handler uses the BatchRepository domain port instead of depending
 * directly on MongoDB infrastructure.
 */
@Slf4j
@Service
public class CustomSupplyDeletedEventHandler {

    private final BatchRepository batchRepository;

    public CustomSupplyDeletedEventHandler(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    /**
     * Handles the CustomSupplyDeletedEvent.
     *
     * @param event event containing the deleted custom supply identifier
     */
    @EventListener
    public void on(CustomSupplyDeletedEvent event) {
        log.info("Handling CustomSupplyDeletedEvent for customSupplyId='{}'", event.customSupplyId());

        var batches = batchRepository.findByCustomSupplyId(event.customSupplyId());

        if (batches.isEmpty()) {
            log.debug("No batches found for customSupplyId='{}'", event.customSupplyId());
            return;
        }

        batches.forEach(batch -> batchRepository.deleteById(batch.getId()));

        log.info(
                "Deleted {} batches related to customSupplyId='{}'",
                batches.size(),
                event.customSupplyId()
        );
    }
}