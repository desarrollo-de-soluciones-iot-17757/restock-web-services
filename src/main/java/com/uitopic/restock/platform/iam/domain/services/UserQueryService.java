package com.uitopic.restock.platform.iam.domain.services;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.queries.GetUserByAccountIdQuery;

import java.util.Optional;

/**
 * Service interface for handling user-related queries.
 * Defines operations for retrieving user information based on specific
 * criteria.
 */
public interface UserQueryService {

    /**
     * Handles the {@link GetUserByAccountIdQuery} to retrieve a user by their
     * account ID.
     * 
     * @param query the query containing the account ID
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> handle(GetUserByAccountIdQuery query);
}
