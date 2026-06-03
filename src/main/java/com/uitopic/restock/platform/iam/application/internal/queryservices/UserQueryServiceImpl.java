package com.uitopic.restock.platform.iam.application.internal.queryservices;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.queries.GetAllUsersByAccountIdQuery;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Query service implementation for {@link User} aggregates.
 * Handles queries related to user retrieval by account ID.
 */
@Slf4j
@Service
public class UserQueryServiceImpl implements UserQueryService {

    // Repository for accessing user data
    private final UserRepository userRepository;

    // Constructor injection of the UserRepository
    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the {@link GetAllUsersByAccountIdQuery} to retrieve all users
     * associated with a given account ID.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link User} aggregates for that account
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> handle(GetAllUsersByAccountIdQuery query) {
        log.debug("Querying all users for account ID: {}", query.accountId());
        var results = userRepository.findAllByAccountId(query.accountId());
        log.debug("Found {} user(s) for account ID: {}", results.size(), query.accountId());
        return results;
    }
}
