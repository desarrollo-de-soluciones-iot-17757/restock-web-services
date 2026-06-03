package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SalesOrderWrapperResource",
        description = "A wrapper resource for a sales order, used for API responses."
)
public record SalesOrderWrapperResource() {
}
