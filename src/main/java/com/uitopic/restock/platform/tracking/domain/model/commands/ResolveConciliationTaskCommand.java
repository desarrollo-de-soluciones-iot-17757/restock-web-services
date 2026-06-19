package com.uitopic.restock.platform.tracking.domain.model.commands;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionAction;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionReason;

/**
 * Command used to resolve a pending conciliation task manually.
 *
 * @param conciliationTaskId identifier of the conciliation task to resolve
 * @param resolvedByUserId identifier of the user resolving the task
 * @param resolutionAction action selected to resolve the discrepancy
 * @param resolutionReason business reason associated with the selected action
 * @param resolutionJustification administrator-provided explanation for the
 *                                resolution
 * @param newJustifiedWithdrawnStock updated justified withdrawn stock value,
 *                                   required only when the selected action is
 *                                   UPDATE_JUSTIFIED_WITHDRAWN_STOCK
 */
public record ResolveConciliationTaskCommand(
        String conciliationTaskId,
        String resolvedByUserId,
        ConciliationResolutionAction resolutionAction,
        ConciliationResolutionReason resolutionReason,
        String resolutionJustification,
        Double newJustifiedWithdrawnStock
) {
    /**
     * Validates required resolution command data.
     */
    public ResolveConciliationTaskCommand {
        if (conciliationTaskId == null || conciliationTaskId.isBlank()) {
            throw new IllegalArgumentException("Conciliation task ID cannot be null or blank");
        }
        if (resolvedByUserId == null || resolvedByUserId.isBlank()) {
            throw new IllegalArgumentException("Resolved by user ID cannot be null or blank");
        }
        if (resolutionAction == null) {
            throw new IllegalArgumentException("Resolution action cannot be null");
        }
        if (resolutionReason == null) {
            throw new IllegalArgumentException("Resolution reason cannot be null");
        }
        if (resolutionJustification == null || resolutionJustification.isBlank()) {
            throw new IllegalArgumentException("Resolution justification cannot be null or blank");
        }
    }
}
