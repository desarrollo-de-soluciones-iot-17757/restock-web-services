package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import lombok.*;

/**
 * Aggregate root representing a branch owned by an account.
 *
 * A branch stores location, image and operational status information.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Branch extends AbstractDomainAggregateRoot<Branch> {

    // Default image URL and public ID for branches without a custom image
    private static final String DEFAULT_IMAGE_URL =
            "https://res.cloudinary.com/deuy1pr9e/image/upload/v1780190808/restock_deafult_branch_image.jpg";

    // Public ID used to identify the default image in the image hosting service
    private static final String DEFAULT_PUBLIC_ID =
            "restock_deafult_branch_image";

    /**
     * Unique identifier for the branch.
     */
    private String id;

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

    /**
     * Creates a new active branch.
     *
     * @param name branch name
     * @param address branch address
     * @param city branch city
     * @param regionOrState branch region or state
     * @param country branch country
     * @param description branch description
     * @param accountId account identifier
     * @param imageUrl branch image URL
     * @param publicId image public ID
     */
    @Builder
    public Branch(
            String name,
            String address,
            String city,
            String regionOrState,
            String country,
            String description,
            String accountId,
            String imageUrl,
            String publicId
    ) {
        validateText(name, "Branch name");
        validateText(address, "Branch address");
        validateText(city, "Branch city");
        validateText(country, "Branch country");
        validateText(accountId, "Account ID");

        this.name = name;
        this.description = description;
        this.location = new Address(address, city, regionOrState, country);
        this.accountId = new AccountId(accountId);
        this.status = BranchStates.ACTIVE;
        applyImage(imageUrl, publicId);
    }

    /**
     * Updates branch .
     *
     * @param name branch name
     * @param description branch description
     * @param address branch address
     * @param city branch city
     * @param regionOrState branch region or state
     * @param country branch country
     * @param imageUrl image URL
     * @param publicId image public ID
     * @return updated branch
     */
    public Branch update(
            String name,
            String description,
            String address,
            String city,
            String regionOrState,
            String country,
            String imageUrl,
            String publicId
    ) {
        validateText(name, "Branch name");
        validateText(address, "Branch address");
        validateText(city, "Branch city");
        validateText(country, "Branch country");

        this.name = name;
        this.description = description;
        this.location = new Address(address, city, regionOrState, country);
        applyImage(imageUrl, publicId);

        return this;
    }

    /**
     * Indicates whether the branch uses the default image.
     *
     * @return true if the default image is used
     */
    public boolean hasDefaultImage() {
        return this.imageUrl != null && DEFAULT_PUBLIC_ID.equals(this.imageUrl.publicId());
    }

    private void applyImage(String imageUrl, String publicId) {
        if (imageUrl == null || imageUrl.isBlank()) {
            this.imageUrl = new ImageURL(DEFAULT_IMAGE_URL, DEFAULT_PUBLIC_ID);
            return;
        }

        this.imageUrl = new ImageURL(imageUrl, publicId);
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    /**
     * Activates the branch.
     */
    public void activate() {
        this.status = BranchStates.ACTIVE;
    }

    /**
     * Deactivates the branch.
     */
    public void deactivate() {
        this.status = BranchStates.INACTIVE;
    }

    /**
     * Changes the branch status.
     *
     * @param status new branch status
     */
    public void changeStatus(BranchStates status) {
        if (status == null) {
            throw new IllegalArgumentException("Branch status cannot be null");
        }

        this.status = status;
    }
}