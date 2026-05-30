package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * This record represents the request resource for creating a new branch. It includes all necessary information
 * required to create a branch, such as account ID, branch name, address, location details, and optional fields
 * like image URL and description.
 */
@Schema(description = "Request resource for creating a branch")
public record CreateBranchResource(
        @NotBlank @Schema(description = "Account ID that owns this branch") String accountId,
        @NotBlank @Schema(description = "Branch name") String name,
        @NotBlank @Schema(description = "Branch address") String address,
        @NotBlank @Schema(description = "State or Region") String stateOrRegion,
        @NotBlank @Schema(description = "City") String city,
        @NotBlank @Schema(description = "Country") String country,
        @Schema(description = "Image URL") String imageUrl,
        @Schema(description = "Branch description") String description
) {}
