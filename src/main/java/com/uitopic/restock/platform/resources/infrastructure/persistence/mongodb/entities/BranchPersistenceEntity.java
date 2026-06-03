package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Persistence entity representing a branch in the MongoDB database.
 * This entity is used to store and retrieve branch information, including its name, description, image URL, location, status, and associated account ID.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "branches")
public class BranchPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * Display name of the branch.
     */
    private String name;

    /**
     * Description of the branch.
     */
    private String description;

    /**
     * Image associated with the branch.
     */
    private ImageURL imageUrl;

    /**
     * Physical location of the branch.
     */
    private Address location;

    /**
     * Current operational status of the branch.
     */
    private BranchStates status;

    /**
     * Account that owns the branch.
     */
    private AccountId accountId;

}
