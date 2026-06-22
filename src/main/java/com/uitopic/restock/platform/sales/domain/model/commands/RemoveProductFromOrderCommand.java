package com.uitopic.restock.platform.sales.domain.model.commands;

/**
 * Command to remove a product from a sales order.
 */
public record RemoveProductFromOrderCommand(
        String orderId,
        String itemId
) {

    /**
     * Constructs a RemoveProductFromOrderCommand with validation.
     */
    public RemoveProductFromOrderCommand {
        validateText(orderId, "Order ID");
        validateText(itemId, "Item ID");
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}

