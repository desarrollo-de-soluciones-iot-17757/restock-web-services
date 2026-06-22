package com.uitopic.restock.platform.sales.domain.model.commands;

public record CompleteSalesOrderCommand(String salesOrderId) {
    public CompleteSalesOrderCommand {
        validateText(salesOrderId, "Sales Order ID");
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}