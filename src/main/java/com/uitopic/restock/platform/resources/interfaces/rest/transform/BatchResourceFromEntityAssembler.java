package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;

/**
 * Assembler class responsible for transforming Batch entities into BatchResource representations. This class provides a static method to convert a Batch entity into a BatchResource, which is a data transfer object (DTO) used for RESTful API responses. The transformation includes mapping relevant fields from the Batch entity to the BatchResource, ensuring that the API response contains the necessary information in a format suitable for client consumption.
 */
public class BatchResourceFromEntityAssembler {

    /**
     * Transforms a Batch entity into a BatchResource. This method takes a Batch entity as input and maps its fields to create a corresponding BatchResource instance. The transformation includes extracting relevant information such as the batch ID, account ID, branch ID, custom supply ID, current quantity, unit name, expiration date, and creation timestamp. The resulting BatchResource is designed to be used in RESTful API responses, providing clients with the necessary data in a structured format.
     *
     * @param entity the Batch entity to be transformed into a BatchResource. This entity contains the data that will be mapped to the BatchResource, including details about the batch, its associated account and branch, and other relevant information.
     * @return a BatchResource instance that represents the transformed Batch entity, containing the mapped fields and ready for use in RESTful API responses. The BatchResource will include the batch ID, account ID, branch ID, custom supply ID, current quantity, unit name, expiration date, and creation timestamp as part of its data structure.
     */
    public static BatchResource toResourceFromEntity(Batch entity) {
        return new BatchResource(
                entity.getId(),
                entity.getCode(),
                entity.getInitialStock().getValue(),
                entity.getCurrentStock().getValue(),
                entity.getInitialStock().getUnit(),
                entity.getUnitPurchaseCost().getAmount().doubleValue(),
                entity.getUnitPurchaseCost().getCurrencyCode(),
                entity.getCustomSupplyId(),
                entity.getReceivingBranchId(),
                entity.getAccountId().accountId(),
                entity.getManufacturingDate().isPresent() ? entity.getManufacturingDate().toString() : null,
                entity.getExpirationDate().isPresent() ? entity.getExpirationDate().toString() : null,
                entity.getEntryDate().isPresent() ? entity.getEntryDate().toString() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}
