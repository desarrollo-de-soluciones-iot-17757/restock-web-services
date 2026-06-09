package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Persistence entity representing a custom supply in the MongoDB database.
 * This entity is used to store and retrieve custom supply information, including its name, associated base supply, stock range, unit price, description, unit of measurement, picture URL, and associated account ID.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "custom_supplies")
public class CustomSupplyPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * Account that owns this custom supply.
     */
    private AccountId accountId;

    /**
     * Custom display name of this supply.
     */
    private String name;

    /**
     * Identifier of the base supply used to create this custom supply.
     */
    private String supplyId;

    /**
     * Full base supply data associated with this custom supply.
     */
    private SupplyPersistenceEntity supply;

    /**
     * Minimum and maximum stock range configured for this custom supply.
     */
    private StockRange stockRange;

    /**
     * Price per unit of this custom supply.
     */
    private Money unitPrice;

    /**
     * Description provided for this custom supply.
     */
    private String description;

    /**
     * Unit of measurement used by this custom supply.
     */
    private UnitMeasurement unitMeasurement;

    /**
     * Optional picture URL associated with this custom supply.
     */
    private ImageURL pictureUrl;
}
