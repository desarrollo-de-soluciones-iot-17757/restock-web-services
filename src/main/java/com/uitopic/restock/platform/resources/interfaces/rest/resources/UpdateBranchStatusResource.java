package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request resource for updating a branch status.
 */
@Schema(description = "Request resource for updating a branch status")
public record UpdateBranchStatusResource(
        @NotNull
        @Schema(description = "Branch status", example = "ACTIVE")
        BranchStates status
) {
}