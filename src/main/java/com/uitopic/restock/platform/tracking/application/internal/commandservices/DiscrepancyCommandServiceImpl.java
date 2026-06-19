package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.domain.model.commands.RegisterDiscrepancyCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreateConciliationTaskCommand;
import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import com.uitopic.restock.platform.tracking.domain.repositories.DiscrepancyRepository;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskCommandService;
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
    private final ConciliationTaskCommandService conciliationTaskCommandService;

    /**
     * @inheritDocs
     */
    @Override
    public void handle(RegisterDiscrepancyCommand command) {
        log.info(
                "{} - Registering a discrepancy for deviceId {}",
                command.getClass().getSimpleName(),
                command.stockComparisonTask().getDeviceId().getDeviceId()
        );

        // Create a new discrepancy entity based on the command data
        var discrepancy = new Discrepancy(
                command.stockComparisonTask(),
                command.riskLevel()
        );

        // Save the discrepancy to the repository
        var savedDiscrepancy = discrepancyRepository.save(discrepancy);

        if (savedDiscrepancy.getRiskLevel() != DiscrepancyAlertLevel.OK) {
            conciliationTaskCommandService.handle(new CreateConciliationTaskCommand(savedDiscrepancy));
        }

        log.info("Discrepancy registered successfully for deviceId: {}", command.stockComparisonTask().getDeviceId().getDeviceId());
    }
}
