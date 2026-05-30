package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request resource for creating a branch")
public record CreateBranchResource(
        @NotBlank @Schema(description = "Account ID that owns this branch") String accountId,
        @NotBlank @Schema(description = "Branch name") String name,
        @NotBlank @Schema(description = "Branch address") String address,
        @NotBlank @Schema(description = "City") String city,
        @NotBlank @Schema(description = "Country") String country,
        @Schema(description = "Image URL") String imageUrl,
        @Schema(description = "Branch description") String description
) {}
