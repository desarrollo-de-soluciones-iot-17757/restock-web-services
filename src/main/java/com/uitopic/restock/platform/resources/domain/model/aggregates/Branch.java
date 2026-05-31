package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Aggregate root representing a physical or logical branch belonging to an account.
 *
 * <p>A branch is the primary organizational unit within the resources bounded context.
 * Each branch is owned by an {@link AccountId} and holds location, status, and optional
 * image information. Branches support soft-delete semantics: deletion transitions the
 * status to {@link BranchStates#INACTIVE} rather than removing the document from MongoDB.
 *
 * <p>Extends {@link com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "branches")
public class Branch extends AuditableAbstractAggregateRoot {

    /** The display name of the branch. Must be unique within the same account. */
    private String name;

    /** A brief human-readable description of the branch. */
    private String description;

    /** Optional URL pointing to an image that represents the branch. */
    private ImageURL imageUrl;

    /** The physical location of the branch, encapsulating address, city, region/state, and country. */
    private Address location;

    /** The current operational status of the branch ({@link BranchStates#ACTIVE} or {@link BranchStates#INACTIVE}). */
    private BranchStates status;

    /** The identifier of the account that owns this branch. */
    private AccountId accountId;

    private static final String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/deuy1pr9e/image/upload/v1780190808/restock_deafult_branch_image.jpg";
    private static final String DEFAULT_PUBLIC_ID = "restock_deafult_branch_image";

    /**
     * Applies a partial update to the branch, only overwriting fields that are non-null.
     * Passing {@code null} for any parameter leaves the corresponding field unchanged.
     *
     * @param location    the new location, or {@code null} to keep the existing one
     * @param description the new description, or {@code null} to keep the existing one
     * @param name        the new name, or {@code null} to keep the existing one
     */
    public void update(Address location, String description, String name) {
        if (name != null && !name.isBlank()) this.name = name;
        if (location != null) this.location = location;
        if (description != null) this.description = description;
    }

    /**
     * Transitions the branch to the {@link BranchStates#INACTIVE} state.
     * Used for logical (soft) deletion of a branch.
     */
    public void deactivate() {
        this.status = BranchStates.INACTIVE;
    }

    /**
     * Transitions the branch to the {@link BranchStates#ACTIVE} state.
     * Used to re-enable a previously deactivated branch.
     */
    public void activate() {
        this.status = BranchStates.ACTIVE;
    }

    /**
     * Returns whether the branch is currently using the default placeholder image.
     */
    public boolean hasDefaultImage() {
        return this.imageUrl != null && DEFAULT_PUBLIC_ID.equals(this.imageUrl.publicId());
    }

    /**
     * Applies an image to the branch. If the provided URL is null or blank,
     * the default placeholder image is used instead.
     *
     * @param imageUrl the URL of the new image, or {@code null} to use the default
     * @param publicId the Cloudinary public ID of the new image
     */
    public void applyImage(String imageUrl, String publicId) {
        if (imageUrl == null || imageUrl.isBlank()) {
            this.imageUrl = new ImageURL(DEFAULT_IMAGE_URL, DEFAULT_PUBLIC_ID);
        } else {
            this.imageUrl = new ImageURL(imageUrl, publicId);
        }
    }
}