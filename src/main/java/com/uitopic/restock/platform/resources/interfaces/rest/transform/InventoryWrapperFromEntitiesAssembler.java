package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.InventoryResource;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.InventoryWrapper;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Assembler to convert a list of {@link Inventory} entities, along with their associated {@link Batch} and {@link CustomSupply} entities, into an {@link InventoryWrapper} DTO within the resources bounded context.
 */
public class InventoryWrapperFromEntitiesAssembler {

    /**
     * Static method to transform Inventory, Batch, and CustomSupply entities into an InventoryResource.
     *
     * @param inventory The Inventory entity containing the inventory details.
     * @param batch The Batch entity containing the batch details associated with the inventory.
     * @param customSupply The CustomSupply entity containing the custom supply details associated with the batch.
     * @return An InventoryResource containing the combined data from the Inventory, Batch, and CustomSupply entities, formatted for the API response.
     */
    private static InventoryResource toResourceFromEntities(@NotNull Inventory inventory, @NotNull Batch batch, @NotNull CustomSupply customSupply) {
        return new InventoryResource(
                customSupply.getName(),
                customSupply.getPictureUrl().getUrl(),
                batch.getCode(),
                inventory.getEntryDate(),
                batch.getExpirationDate(),
                inventory.getMinimumStock().getMinimumStock(),
                inventory.getCurrentStock().getValue(),
                inventory.getMinimumStock().getUnitMeasurement(),
                inventory.getState().name()
        );
    }

    /**
     * Static method to transform a list of Inventory entities into an InventoryWrapper.
     *
     * @param branchId The unique identifier of the branch associated with the inventory items.
     * @param inventories The list of Inventory entities to be transformed.
     * @param batches The list of Batch entities to be used for finding the corresponding batch for each inventory item.
     * @param customSupplies The list of CustomSupply entities to be used for finding the corresponding custom supply for each batch.
     * @return An InventoryWrapper containing the data from the entities, including the total count and the list of InventoryResources.
     */
    public static InventoryWrapper toWrapperFromEntities(String branchId, List<Inventory> inventories, List<Batch> batches, List<CustomSupply> customSupplies) {

        // Transform each Inventory entity into an InventoryResource by finding the corresponding Batch and CustomSupply entities, and collect them into a list.
        var inventoryResources = inventories.stream()
                .map(inventory -> {
                    var batch = batches.stream()
                            .filter(b -> b.getId().equals(inventory.getBatchId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Batch not found for inventory with batchId: " + inventory.getBatchId()));

                    var customSupply = customSupplies.stream()
                            .filter(cs -> cs.getId().equals(batch.getCustomSupplyId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("CustomSupply not found for batch with customSupplyId: " + batch.getCustomSupplyId()));

                    return toResourceFromEntities(inventory, batch, customSupply);
                })
                .toList();

        // Create and return a new InventoryWrapper with the branchId, total count of inventory items, and the list of InventoryResources.
        return new InventoryWrapper(
                branchId,
                inventories.size(),
                inventoryResources
        );
    }
}
