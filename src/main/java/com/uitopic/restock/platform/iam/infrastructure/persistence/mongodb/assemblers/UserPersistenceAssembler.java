package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.entities.UserPersistenceEntity;

/**
 * Assembler class responsible for converting between User domain model instances and UserPersistenceEntity instances
 * used for MongoDB storage. This class provides static methods to perform the necessary field mappings between the
 * two representations of the user data.
 */
public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a UserPersistenceEntity from MongoDB to a User domain model instance.
     *
     * @param entity the UserPersistenceEntity instance to convert
     * @return a User domain model instance with fields mapped from the UserPersistenceEntity
     */
    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new User();
        domain.setId(entity.getId());
        domain.setEmail(entity.getEmail());
        domain.setPasswordHash(entity.getPasswordHash());
        domain.setRole(entity.getRole());
        domain.setAccountId(entity.getAccountId());
        return domain;
    }

    /**
     * Converts a User domain model instance to a UserPersistenceEntity for MongoDB storage.
     *
     * @param user the User domain model instance to convert
     * @return a UserPersistenceEntity instance with fields mapped from the User domain model
     */
    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        if (user == null) return null;
        var entity = new UserPersistenceEntity();
        if (user.getId() != null) {
            entity.setId(user.getId());
        }
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(user.getRole());
        entity.setAccountId(user.getAccountId());
        return entity;
    }
}
