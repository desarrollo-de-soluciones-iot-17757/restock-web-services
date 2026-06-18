package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request resource to create a sales order with items")
public record CreateSalesOrderResource(
        @NotBlank @Schema(description = "Branch ID")
        String branchId
) {}