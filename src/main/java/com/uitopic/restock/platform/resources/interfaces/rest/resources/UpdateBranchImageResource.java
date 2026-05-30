package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request resource for updating branch image")
public record UpdateBranchImageResource(
        @Schema(description = "Image URL") String imageUrl
) {}
