package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Persistence entity representing a batch of stock in a branch.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "batches")
public class BatchPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * Business code used to identify the batch.
     */
    private String code;

    /**
     * Current stock available in this batch.
     */
    private Stock currentStock;

    /**
     * Identifier of the custom supply associated with this batch.
     */
    private String customSupplyId;

    /**
     * Identifier of the branch where this batch is stored.
     */
    private String branchId;

    /**
     * Account that owns this batch.
     */
    private AccountId accountId;

    /**
     * Optional expiration date of the batch.
     */
    private LocalDate expirationDate;

    /**
     * Date when the batch entered the inventory.
     */
    private LocalDate entryDate;
}
