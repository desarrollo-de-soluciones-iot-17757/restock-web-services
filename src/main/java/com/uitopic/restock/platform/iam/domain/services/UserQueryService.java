package com.uitopic.restock.platform.iam.domain.services;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.queries.GetAllUsersByAccountIdQuery;

import java.util.List;

/**
 * Service interface for handling user-related queries.
 * Defines operations for retrieving user information based on specific criteria.
 */
public interface UserQueryService {

    /**
     * Handles the {@link GetAllUsersByAccountIdQuery} to retrieve all users
     * associated with a given account ID.
     * Designed to support accounts with multiple worker users.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link User} aggregates for that account
     */
    List<User> handle(GetAllUsersByAccountIdQuery query);
}
