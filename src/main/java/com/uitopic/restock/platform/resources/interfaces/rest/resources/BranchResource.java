package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response resource representing a branch")
public record BranchResource(
        @Schema(description = "Unique identifier of the branch") String id,
        @Schema(description = "Account that owns this branch") String accountId,
        @Schema(description = "Branch name") String name,
        @Schema(description = "Branch address") String address,
        @Schema(description = "City where the branch is located") String city,
        @Schema(description = "Country where the branch is located") String country,
        @Schema(description = "URL of the branch image") String imageUrl,
        @Schema(description = "Status of the branch", example = "active") String status,
        @Schema(description = "Branch description") String description,
        @Schema(description = "Creation timestamp") String createdAt
) {}
