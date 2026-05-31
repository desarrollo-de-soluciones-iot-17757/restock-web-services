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

    private static final String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/deuy1pr9e/image/upload/v1780190808/restock_deafult_branch_image.jpg";
    private static final String DEFAULT_PUBLIC_ID = "restock_deafult_branch_image";

    /** Constructor for creating a new Branch instance. This constructor initializes the branch with the provided name, description, location, image URL, and account ID. The status of the branch is set to active by default. */
    public Branch(String name, String description, String address, String city, String country, String regionOrState, String accountId, String imageUrl, String publicId) {
        this.name = name;
        this.description = description;
        this.location = new Address(address, city, regionOrState, country);
        this.accountId = new AccountId(accountId);
        this.status = BranchStates.ACTIVE;
        this.applyImage(imageUrl, publicId);
    }

    /** The ID of the tenant that this branch belongs to. This is used for multi-tenancy support. */
    public Branch updateBranch(String name, String address, String city, String country, String regionOrState, String description, String imageUrl, String publicId) {
        if (name != null && !name.isBlank()) this.name = name;
        if (address != null) this.location = new Address(address, city, regionOrState, country);
        this.applyImage(imageUrl, publicId);
        if (description != null) this.description = description;
        return this;
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

    /** Static method to return the default image URL for branches. This method creates and returns an ImageURL object with the default image URL and public ID. */
    public boolean hasDefaultImage() {
        return DEFAULT_PUBLIC_ID.equals(this.imageUrl.publicId());
    }

    /** Method to apply an image to the branch. This method takes an image URL and public ID as parameters and updates the branch's imageUrl field accordingly. It returns the updated Branch instance. */
    public void applyImage(String imageUrl, String publicId) {
        if (imageUrl == null || imageUrl.isBlank()) {
            this.imageUrl = new ImageURL(DEFAULT_IMAGE_URL, DEFAULT_PUBLIC_ID);
        } else {
            this.imageUrl = new ImageURL(imageUrl, publicId);
        }
    }
}
