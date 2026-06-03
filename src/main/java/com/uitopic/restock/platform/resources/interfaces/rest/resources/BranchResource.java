package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a branch.
 */
@Schema(description = "Response resource representing a branch")
public record BranchResource(
        @Schema(description = "Branch ID")
        String id,

        @Schema(description = "Account ID")
        String accountId,

        @Schema(description = "Branch name")
        String name,

        @Schema(description = "Branch description")
        String description,

        @Schema(description = "Branch address")
        String address,

        @Schema(description = "Branch city")
        String city,

        @Schema(description = "Branch region or state")
        String regionOrState,

        @Schema(description = "Branch country")
        String country,

        @Schema(description = "Branch status")
        String status,

        @Schema(description = "Branch image URL")
        String imageUrl,

        @Schema(description = "Cloudinary public ID")
        String imagePublicId
) {
}