package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for creating a branch using multipart/form-data.
 */
@Schema(description = "Request resource for creating a branch")
public record CreateBranchResource(
        @NotBlank
        @Schema(description = "Branch name")
        String name,

        @NotBlank
        @Schema(description = "Branch address")
        String address,

        @NotBlank
        @Schema(description = "Branch city")
        String city,

        @Schema(description = "Branch region or state")
        String regionOrState,

        @NotBlank
        @Schema(description = "Branch country")
        String country,

        @Schema(description = "Branch description")
        String description,

        @Schema(description = "Branch image file")
        MultipartFile image
) {
}