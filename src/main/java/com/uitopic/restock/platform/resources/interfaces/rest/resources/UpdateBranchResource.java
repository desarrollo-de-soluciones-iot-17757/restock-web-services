package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for partially updating a branch.
 *
 * All fields are optional. Only provided fields are applied.
 */
@Schema(description = "Request resource for partially updating a branch")
public record UpdateBranchResource(
        @Schema(description = "Branch name")
        String name,

        @Schema(description = "Branch address")
        String address,

        @Schema(description = "Branch city")
        String city,

        @Schema(description = "Branch region or state")
        String regionOrState,

        @Schema(description = "Branch country")
        String country,

        @Schema(description = "Branch description")
        String description,

        @Schema(description = "Branch image file")
        MultipartFile image
) {
}