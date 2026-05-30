package com.uitopic.restock.platform.iam.domain.model.aggregates;

import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the User aggregate root for the IAM bounded context.
 * Stores authentication credentials and role assignment for a given account.
 *
 * <p>The {@code email} field is of type {@link Email} in the domain model.
 * A MongoDB converter handles serialization to/from a plain string value,
 * so it is stored as a primitive in the database — not as an embedded document.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends AuditableAbstractAggregateRoot {

    /**
     * The email address of the user, which serves as their unique identifier.
     * Stored as a plain string in MongoDB via a registered converter.
     */
    @Indexed(unique = true)
    private Email email;

    /**
     * The BCrypt-hashed password used to authenticate the user.
     */
    private String passwordHash;

    /**
     * The role assigned to the user, specifying their authorization privileges.
     */
    private Role role;

    /**
     * The associated identifier of the core account linked to this IAM user.
     */
    private AccountId accountId;

    /**
     * Updates the user's hashed password.
     *
     * @param passwordHash the new BCrypt-hashed password
     */
    public void update(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Updates the user's email address.
     *
     * @param email the new email value object to set
     */
    public void update(Email email) {
        this.email = email;
    }

    /**
     * Updates the user's role assignment.
     *
     * @param role the new role to set
     */
    public void update(Role role) {
        this.role = role;
    }
}
