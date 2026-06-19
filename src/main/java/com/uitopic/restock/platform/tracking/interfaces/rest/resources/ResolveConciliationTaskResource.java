package com.uitopic.restock.platform.tracking.interfaces.rest.resources;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionAction;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationResolutionReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body used to manually resolve a conciliation task.
 *
 * @param resolvedByUserId identifier of the administrator resolving the task
 * @param resolutionAction action selected to resolve the discrepancy
 * @param resolutionReason business reason associated with the selected action
 * @param resolutionJustification explanation provided by the administrator
 * @param newJustifiedWithdrawnStock new justified withdrawn stock value,
 *                                   required when the action is
 *                                   UPDATE_JUSTIFIED_WITHDRAWN_STOCK
 */
public record ResolveConciliationTaskResource(
        @NotBlank(message = "Resolved by user ID is required")
        String resolvedByUserId,

        @NotNull(message = "Resolution action is required")
        ConciliationResolutionAction resolutionAction,

        @NotNull(message = "Resolution reason is required")
        ConciliationResolutionReason resolutionReason,

        @NotBlank(message = "Resolution justification is required")
        String resolutionJustification,

        Double newJustifiedWithdrawnStock
) {
}
