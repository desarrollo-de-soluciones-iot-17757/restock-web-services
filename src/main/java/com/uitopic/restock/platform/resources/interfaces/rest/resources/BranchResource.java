package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a branch.
 *
 * @param id            Unique identifier of the branch
 * @param accountId     Account that owns this branch
 * @param name          Branch name
 * @param address       Branch address
 * @param city          City where the branch is located
 * @param stateOrRegion State or Region where the branch is located
 * @param country       Country where the branch is located
 * @param imageUrl      URL of the branch image
 * @param status        Status of the branch (e.g., active, inactive)
 * @param description   Branch description
 * @param createdAt     Creation timestamp
 */
@Schema(description = "Response resource representing a branch")
public record BranchResource(
        @Schema(description = "Unique identifier of the branch") String id,
        @Schema(description = "Account that owns this branch") String accountId,
        @Schema(description = "Branch name") String name,
        @Schema(description = "Branch address") String address,
        @Schema(description = "City where the branch is located") String city,
        @Schema(description = "State or Region where the branch is located") String stateOrRegion,
        @Schema(description = "Country where the branch is located") String country,
        @Schema(description = "URL of the branch image") String imageUrl,
        @Schema(description = "Status of the branch", example = "active") String status,
        @Schema(description = "Branch description") String description,
        @Schema(description = "Creation timestamp") String createdAt
) {}
