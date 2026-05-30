package com.uitopic.restock.platform.iam.application.internal.queryservices;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.queries.GetUserByAccountIdQuery;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Query service implementation for {@link User} aggregates.
 * Handles queries related to user retrieval by account ID.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    /**
     * Constructor for {@code UserQueryServiceImpl}.
     * 
     * @param userRepository the repository for {@link User} aggregates
     */
    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the {@link GetUserByAccountIdQuery} to retrieve a user by their
     * account ID.
     * 
     * @param query the query containing the account ID
     * @return an {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    @Override
    public Optional<User> handle(GetUserByAccountIdQuery query) {
        return userRepository.findByAccountId(query.accountId());
    }
}
