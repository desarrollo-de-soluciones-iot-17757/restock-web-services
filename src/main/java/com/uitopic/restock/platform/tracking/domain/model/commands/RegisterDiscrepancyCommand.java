package com.uitopic.restock.platform.tracking.domain.model.commands;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.exceptions.DiscrepancyResolutionException;

/**
 * Command representing the registration of a discrepancy in inventory tracking.
 *
 * @param reportedQuantity the quantity reported by the device, which may differ from the expected quantity
 * @param deviceId the unique identifier of the device reporting the discrepancy
 */
public record RegisterDiscrepancyCommand(
        Double reportedQuantity,
        DeviceId deviceId
) {

    public RegisterDiscrepancyCommand {
        if (reportedQuantity == null || reportedQuantity < 0) {
            throw new DiscrepancyResolutionException("Reported quantity must be a non-negative number");
        }
    }
}
