package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Aggregate root representing a Branch in the system. A Branch is associated with an Account and contains information such as name, description, location, status, and an optional image URL.
 * This class extends AuditableAbstractAggregateRoot to include auditing fields like createdAt and updatedAt.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "branches")
public class Branch extends AuditableAbstractAggregateRoot {

    /** The name of the branch. This field is required and must be unique within the same account. */
    private String name;

    /** A brief description of the branch. */
    private String description;

    /** The URL of an optional image associated with the branch. */
    private ImageURL imageUrl;

    /** The location of the branch, including address, city, region/state, and country. */
    private Address location;

    /** The status of the branch, which can be either active or inactive. */
    private BranchStates status;

    /** The ID of the account that owns this branch. */
    private AccountId accountId;

    /** The ID of the tenant that this branch belongs to. This is used for multi-tenancy support. */
    public void update(String address, String city, String country, String regionOrState, String description, String name) {
        if (name != null && !name.isBlank()) this.name = name;
        if (address != null) this.location = new Address(address, city, regionOrState, country);
        if (description != null) this.description = description;
    }

    /**
     * Methods to activate and deactivate the branch. These methods change the status of the branch accordingly.
     */
    public void deactivate() {
        this.status = BranchStates.INACTIVE;
    }

    /**
     * Method to activate the branch. This method changes the status of the branch to active.
     */
    public void activate() {
        this.status = BranchStates.ACTIVE;
    }
}
