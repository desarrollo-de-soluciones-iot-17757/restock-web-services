package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

/**
 * Command to create a new custom supply.
 */
public record CreateCustomSupplyCommand(
        String accountId,
        String name,
        StockRange stockRange,
        String supplyId,
        Money unitPrice,
        String description,
        UnitMeasurement unitMeasurement,
        byte[] image,
        String photoFileName
) {
    public CreateCustomSupplyCommand {
        validateText(accountId, "Account ID");
        validateText(name, "Custom supply name");
        validateText(supplyId, "Supply ID");

        if (stockRange == null) {
            throw new IllegalArgumentException("Stock range cannot be null");
        }

        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price cannot be null");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }

        if (unitMeasurement == null) {
            throw new IllegalArgumentException("Unit measurement cannot be null");
        }
    }

    /**
     * Indicates whether the command contains a new image.
     *
     * @return true if image data and file name are present
     */
    public boolean hasNewPhoto() {
        return image != null && image.length > 0
                && photoFileName != null && !photoFileName.isBlank();
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}