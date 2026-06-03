package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "SalesOrderItemResource",
    description = "Represents an item in a sales order, including product details and quantity."
)
public record SalesOrderItemResource() {
}
