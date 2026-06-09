package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

/**
 * Command to partially update an existing custom supply.
 *
 * The base supply ID is intentionally not editable from this command.
 */
public record UpdateCustomSupplyCommand(
        String customSupplyId,
        String name,
        String description,
        StockRange stockRange,
        Money unitPrice,
        UnitMeasurement unitMeasurement,
        byte[] image,
        String photoFileName
) {
    public UpdateCustomSupplyCommand {
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
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
}