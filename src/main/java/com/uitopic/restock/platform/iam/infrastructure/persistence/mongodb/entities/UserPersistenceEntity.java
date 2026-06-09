package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Persistence entity representing a User document in MongoDB for the IAM bounded context.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "users")
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {

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
}
