package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.domain.model.commands.RegisterDiscrepancyCommand;
import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.repositories.DiscrepancyRepository;
import com.uitopic.restock.platform.tracking.domain.services.DiscrepancyCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the DiscrepancyCommandService interface for handling inventory discrepancy commands.
 * This service is responsible for processing commands related to inventory discrepancies, such as registering new discrepancies.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscrepancyCommandServiceImpl implements DiscrepancyCommandService {

    // Repository for managing discrepancies
    private final DiscrepancyRepository discrepancyRepository;

    /**
     * @inheritDocs
     */
    @Override
    public void handle(RegisterDiscrepancyCommand command) {
        log.info(
                "{} - Registering a discrepancy for deviceId {}",
                command.getClass().getSimpleName(),
                command.deviceId().getDeviceId()
        );

        // Create a new discrepancy entity based on the command data
        var discrepancy = new Discrepancy(
                command.reportedQuantity(),
                command.riskLevel(),
                command.deviceId()
        );

        // Save the discrepancy to the repository
        discrepancyRepository.save(discrepancy);

        log.info("Discrepancy registered successfully for deviceId: {}", command.deviceId().getDeviceId());

        // TODO: Create the conciliation task here and associate it with the discrepancy if needed
    }
}
