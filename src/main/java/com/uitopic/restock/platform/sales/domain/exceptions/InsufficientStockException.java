package com.uitopic.restock.platform.sales.domain.exceptions;

/**
 * Exception thrown when there is insufficient physical stock to fulfill a
 * sales order item. Carries the structured data the client needs to render
 * the "Action Blocked: Insufficient Physical Inventory" dialog, instead of a
 * plain message.
 */
public class InsufficientStockException extends RuntimeException {

    private final String customSupplyId;
    private final String supplyName;
    private final Double quantityNeeded;
    private final Double quantityAvailable;

    public InsufficientStockException(String customSupplyId, String supplyName, Double quantityNeeded, Double quantityAvailable) {
        super("Insufficient stock for '" + supplyName + "': needed " + quantityNeeded + ", available " + quantityAvailable);
        this.customSupplyId = customSupplyId;
        this.supplyName = supplyName;
        this.quantityNeeded = quantityNeeded;
        this.quantityAvailable = quantityAvailable;
    }

    public String getCustomSupplyId() {
        return customSupplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public Double getQuantityNeeded() {
        return quantityNeeded;
    }

    public Double getQuantityAvailable() {
        return quantityAvailable;
    }
}

