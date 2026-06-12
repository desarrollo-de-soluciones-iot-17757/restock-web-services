package com.uitopic.restock.platform.iam.application.acl;

import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.interfaces.acl.IamContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the IAM anti-corruption layer facade.
 * Delegates to the domain repository to answer queries from external bounded contexts.
 * Does not expose internal domain objects — returns primitive types only.
 */
@Slf4j
@Service
public class IamContextFacadeImpl implements IamContextFacade {

    // The UserRepository is used to query user data without exposing domain entities.
    private final UserRepository userRepository;

    // Constructor injection of the UserRepository dependency.
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
    @Transactional(readOnly = true)
    public boolean existsUserByEmail(String email) {
        log.debug("Checking existence of user with email: {}", email);
        boolean exists = userRepository.existsByEmail(new Email(email));
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
    @Transactional(readOnly = true)
    public String fetchAccountIdByUserId(String userId) {
        log.debug("Fetching account ID for user ID: {}", userId);
        String accountId = userRepository.findById(userId)
                .map(user -> user.getAccountId() != null ? user.getAccountId().getAccountId() : "")
                .orElse("");
        log.debug("Account ID for user ID {}: '{}'", userId, accountId);
        return accountId;
    }

    /**
     * Retrieves the usernames associated with the given account ID.
     *
     * @param accountId the ID of the account
     * @return a list of usernames (email addresses) associated with the account, or an empty list if none found
     */
    @Override
    public List<String> getUsernamesByAccountId(AccountId accountId) {
        var users = userRepository.findByAccountId(accountId);

        return users.stream()
                .map(user -> user.getEmail().getEmail())
                .toList();
    }
}
