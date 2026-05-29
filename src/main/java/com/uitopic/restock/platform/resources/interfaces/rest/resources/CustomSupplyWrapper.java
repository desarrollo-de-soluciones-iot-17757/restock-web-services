package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response resource representing a wrapper for a list of custom supplies associated with an account.
 *
 * @param accountId Unique identifier of the account
 * @param totalSupplies Total number of custom supplies
 * @param supplies List of custom supplies associated with the account
 */
@Schema(description = "Wrapper for a list of custom supplies associated with an account")
public record CustomSupplyWrapper(
        @Schema(description = "Unique identifier of the account", example = "123e4567-e89b-12d3-a456-426614174000")
        String accountId,
        @Schema(description = "Total number of custom supplies", example = "5")
        int totalSupplies,
        @Schema(description = "List of custom supplies")
        List<CustomSupplyItem> supplies
) {

    /**
     * Adds a custom supply item to the wrapper and updates the total supplies count.
     *
     * @param item The custom supply item to be added
     */
    public void addItem(CustomSupplyItem item) {
        this.supplies.add(item);
    }
}
