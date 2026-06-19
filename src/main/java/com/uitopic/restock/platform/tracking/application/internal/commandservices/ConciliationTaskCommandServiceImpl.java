package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UserId;
import com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring.SpringDomainEventPublisher;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalDevicesService;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.tracking.domain.exceptions.DiscrepancyResolutionException;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.ClosePendingConciliationTasksCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreateConciliationTaskCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.ResolveConciliationTaskCommand;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionAction;
import com.uitopic.restock.platform.tracking.domain.repositories.ConciliationTaskRepository;
import com.uitopic.restock.platform.tracking.domain.repositories.DiscrepancyRepository;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Application command service for conciliation task workflows.
 *
 * <p>
 * Coordinates the creation of conciliation tasks from discrepancies,
 * executes manual resolution actions requested by administrators, resolves the
 * originating discrepancy, and publishes domain events produced by the
 * aggregate. It also handles automatic closure when later telemetry confirms
 * that stock has normalized.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConciliationTaskCommandServiceImpl implements ConciliationTaskCommandService {
    private final ConciliationTaskRepository conciliationTaskRepository;
    private final DiscrepancyRepository discrepancyRepository;
    private final ExternalDevicesService externalDevicesService;
    private final ExternalResourcesService externalResourcesService;
    private final SpringDomainEventPublisher eventPublisher;

    /**
     * Creates a pending conciliation task from a discrepancy that requires conciliation.
     *
     * @param command command with the discrepancy that originated from
     *                a mismatched stock comparison
     * @return persisted conciliation task
     */
    @Override
    public ConciliationTask handle(CreateConciliationTaskCommand command) {
        var task = new ConciliationTask(command.discrepancy());
        return conciliationTaskRepository.save(task);
    }

    /**
     * Resolves a pending conciliation task using the action provided by the
     * frontend.
     *
     * <p>
     * Depending on the selected action, this method may call external ACL
     * services to update digital stock or justified withdrawn stock before
     * persisting the resolved task.
     *
     * @param command command containing the conciliation task identifier and
     *                manual resolution data
     * @return resolved conciliation task
     */
    @Override
    public ConciliationTask handle(ResolveConciliationTaskCommand command) {
        if (command.resolutionAction() == ConciliationResolutionAction.AUTOMATIC_CLOSE) {
            throw new DiscrepancyResolutionException("Automatic close cannot be requested manually");
        }

        var task = conciliationTaskRepository.findById(command.conciliationTaskId())
                .orElseThrow(() -> new DiscrepancyResolutionException("Conciliation task not found: " + command.conciliationTaskId()));

        var userId = new UserId(command.resolvedByUserId());
        switch (command.resolutionAction()) {
            case ADJUST_DIGITAL_STOCK -> {
                task.resolveByDigitalStockAdjustment(userId, command.resolutionReason(), command.resolutionJustification());
                externalResourcesService.adjustDigitalStock(
                        task.getBatchId(),
                        task.getTotalPhysicalStock());
            }
            case UPDATE_JUSTIFIED_WITHDRAWN_STOCK -> {
                if (command.newJustifiedWithdrawnStock() == null) {
                    throw new DiscrepancyResolutionException("New justified withdrawn stock is required");
                }
                externalDevicesService.updateJustifiedWithdrawnStock(task.getDeviceId(), command.newJustifiedWithdrawnStock());
                task.resolveByJustifiedWithdrawnStockUpdate(userId, command.newJustifiedWithdrawnStock(), command.resolutionJustification());
            }
            case RECALIBRATE_DEVICE -> {
                externalDevicesService.recalibrateDevice(task.getDeviceId());
                task.resolveByDeviceRecalibration(userId, command.resolutionJustification());
            }
            case AUTOMATIC_CLOSE -> throw new DiscrepancyResolutionException("Automatic close cannot be requested manually");
        }

        resolveDiscrepancy(task.getDiscrepancyId());
        var saved = conciliationTaskRepository.save(task);
        publishEvents(task);
        return saved;
    }

    /**
     * Automatically resolves pending conciliation tasks when a new stock
     * comparison confirms that the same account, device, custom supply and
     * batch no longer have a discrepancy.
     *
     * @param command command with the matching stock comparison task used to
     *                locate pending conciliation tasks in the same scope
     */
    @Override
    public void handle(ClosePendingConciliationTasksCommand command) {
        var stockComparisonTask = command.stockComparisonTask();
        var pendingTasks = conciliationTaskRepository.findPendingByScope(
                stockComparisonTask.getAccountId(),
                stockComparisonTask.getDeviceId(),
                stockComparisonTask.getCustomSupplyId(),
                stockComparisonTask.getBatchId());

        pendingTasks.forEach(task -> {
            task.resolveAutomatically();
            resolveDiscrepancy(task.getDiscrepancyId());
            conciliationTaskRepository.save(task);
            publishEvents(task);
        });
    }

    /**
     * Marks the discrepancy related to a conciliation task as resolved.
     *
     * @param discrepancyId discrepancy identifier associated with the
     *                      conciliation task
     */
    private void resolveDiscrepancy(String discrepancyId) {
        discrepancyRepository.findById(discrepancyId).ifPresent(discrepancy -> {
            if (discrepancy.getStatus() != com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyStatus.RESOLVED) {
                discrepancy.resolve();
                discrepancyRepository.save(discrepancy);
            }
        });
    }

    /**
     * Publishes and clears domain events registered by the conciliation task.
     *
     * @param task conciliation task that produced domain events
     */
    private void publishEvents(ConciliationTask task) {
        task.domainEvents().forEach(eventPublisher::publish);
        task.clearDomainEvents();
    }
}
