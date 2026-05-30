package com.uitopic.restock.platform.iam.interfaces.rest.transform;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler for converting {@link User} entities to {@link UserResource} and
 * {@link AuthenticatedUserResource}.
 * <p>
 * This class provides static methods to transform {@link User} aggregates from
 * the domain model into the corresponding REST resource representations.
 * It is used to map the internal domain objects to DTOs that can be returned
 * by the REST API.
 */
public class UserResourceFromEntityAssembler {

    /**
     * Converts a {@link User} entity and an authentication token into an
     * {@link AuthenticatedUserResource}.
     * 
     * @param user  the {@link User} aggregate to convert
     * @param token the authentication token associated with the user
     * @return an {@link AuthenticatedUserResource} representing the user and their
     *         token
     */
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(
                user.getId(),
                user.getEmail().email(),
                user.getRole().getType().name(),
                token);
    }

    /**
     * Converts a {@link User} entity into a {@link UserResource}.
     * 
     * @param user the {@link User} aggregate to convert
     * @return a {@link UserResource} representing the user
     */
    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId(),
                user.getEmail().email(),
                user.getRole().getType().name());
    }
}
