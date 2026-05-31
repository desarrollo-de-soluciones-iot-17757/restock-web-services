package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyItem;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.CustomSupplyWrapper;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Assembler to convert a list of {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply} entities
 * to a {@link CustomSupplyWrapper} DTO within the resources bounded context.
 */
public class CustomSupplyWrapperFromEntitiesAssembler {

    /**
     * Static method to transform a CustomSupply entity into a CustomSupplyItem.
     *
     * @param entity The CustomSupply entity to be transformed.
     * @return A CustomSupplyItem containing the data from the entity, excluding the accountId which is not needed for the item representation in the wrapper.
     */
    private static CustomSupplyItem toItemFromEntity(@NotNull CustomSupply entity) {
        return new CustomSupplyItem(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory().getName(),
                entity.getUnitPrice().getAmount().toString(),
                entity.getUnitPrice().getCurrencyCode(),
                entity.getSupplyContent().getContent(),
                entity.getUnitMeasurement().getUnitName(),
                entity.getPictureUrl() != null ? entity.getPictureUrl().getUrl() : null
        );
    }

    /**
     * Static method to transform a list of CustomSupply entities into a CustomSupplyWrapper.
     *
     * @param accountId The AccountId associated with the CustomSupplies.
     * @param entities The list of CustomSupply entities to be transformed.
     * @return A CustomSupplyWrapper containing the data from the entities, including the total count and the list of CustomSupplyItems.
     */
    public static CustomSupplyWrapper toWrapperFromEntities(AccountId accountId, List<CustomSupply> entities) {

        // Transform each CustomSupply entity into a CustomSupplyItem and collect them into a list.
        var customSupplies = entities.stream()
                .map(CustomSupplyWrapperFromEntitiesAssembler::toItemFromEntity)
                .toList();

        // Create and return a new CustomSupplyWrapper with the accountId, total count of custom supplies, and the list of custom supply items.
        return new CustomSupplyWrapper(
                accountId.getAccountId(),
                customSupplies.size(),
                customSupplies
        );
    }
}
