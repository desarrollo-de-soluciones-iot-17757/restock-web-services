package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Represents the inventory state of a product. It can be one of the following values:
 * - OUT_OF_STOCK: The product is currently out of stock and cannot be sold.
 * - LOW_STOCK: The product has a low stock level and may need to be restocked soon.
 * - IN_STOCK: The product is currently in stock and available for sale.
 * - OVERSTOCKED: The product has an excessive stock level and may need to be reduced or promoted to avoid overstocking issues.
 */
public enum InventoryState {
    OUT_OF_STOCK,
    LOW_STOCK,
    IN_STOCK,
    OVERSTOCKED
}
