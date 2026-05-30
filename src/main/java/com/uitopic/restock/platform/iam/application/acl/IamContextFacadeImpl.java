package com.uitopic.restock.platform.iam.application.acl;

import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.interfaces.acl.IamContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IAM anti-corruption layer facade.
 * Delegates to the domain repository to answer queries from external bounded contexts.
 * Does not expose internal domain objects — returns primitive types only.
 */
@Slf4j
@Service
public class IamContextFacadeImpl implements IamContextFacade {

    private final UserRepository userRepository;

    public IamContextFacadeImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Checks whether a user with the given email exists in the system.
     *
     * @param email the email address to check
     * @return true if a user with that email is registered, false otherwise
     */
    @Override
    public boolean existsUserByEmail(String email) {
        log.debug("Checking existence of user with email: {}", email);
        boolean exists = userRepository.existsByEmail(email);
        log.debug("User with email {} exists: {}", email, exists);
        return exists;
    }

    /**
     * Retrieves the account ID associated with the given user ID.
     * Returns an empty string if the user does not exist or has no account assigned.
     *
     * @param userId the ID of the user
     * @return the account ID as a string, or empty string if not found
     */
    @Override
    public String fetchAccountIdByUserId(String userId) {
        log.debug("Fetching account ID for user ID: {}", userId);
        String accountId = userRepository.findById(userId)
                .map(user -> user.getAccountId() != null ? user.getAccountId().getAccountId() : "")
                .orElse("");
        log.debug("Account ID for user ID {}: '{}'", userId, accountId);
        return accountId;
    }
}
