package com.uitopic.restock.platform.sales.domain.model.commands;

import com.uitopic.restock.platform.sales.domain.model.valueobjects.ProductType;

import java.math.BigDecimal;

/**
 * Command to add a product (Kit or Recipe) to a sales order.
 */
public record AddProductToOrderCommand(
        String orderId,
        String productId,
        ProductType productType,
        String nameSnapshot,
        BigDecimal unitPrice,
        String currency,
        int quantity
) {

    /**
     * Constructs an AddProductToOrderCommand with validation.
     */
    public AddProductToOrderCommand {
        validateText(orderId, "Order ID");
        validateText(productId, "Product ID");
        validateText(nameSnapshot, "Name snapshot");
        validateText(currency, "Currency");

        if(orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        if (productType == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }

        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}

