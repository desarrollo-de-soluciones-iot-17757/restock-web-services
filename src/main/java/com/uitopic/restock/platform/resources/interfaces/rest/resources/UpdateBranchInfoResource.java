package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request resource for updating branch info")
public record UpdateBranchInfoResource(
        @Schema(description = "Branch name") String name,
        @Schema(description = "Branch address") String address,
        @Schema(description = "City") String city,
        @Schema(description = "State or Region") String regionOrState,
        @Schema(description = "Country") String country,
        @Schema(description = "Branch description") String description
) {}
