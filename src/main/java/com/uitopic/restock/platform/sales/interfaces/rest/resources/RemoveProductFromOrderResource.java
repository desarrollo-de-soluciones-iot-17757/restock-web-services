package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RemoveProductFromOrderResource(
        @NotBlank @Schema(description = "Item ID to remove")
        String itemId
) {}