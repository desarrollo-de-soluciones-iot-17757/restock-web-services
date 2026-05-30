package com.uitopic.restock.platform.iam.application.acl;

import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IAM anti-corruption layer facade.
 * Delegates to the domain repository to answer queries from external bounded contexts.
 * Does not expose internal domain objects — returns primitive types only.
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {

    private final UserRepository userRepository;

    /**
     * Constructs the facade with the required user repository.
     *
     * @param userRepository port for user persistence operations
     */
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
        return userRepository.existsByEmailValue(email);
    }

    /**
     * Retrieves the account ID associated with the given user ID.
     * Returns an empty string if the user does not exist or has no account assigned.
     * accountId is null until Account bounded context integration is complete.
     *
     * @param userId the ID of the user
     * @return the account ID as a string, or empty string if not found
     */
    @Override
    public String fetchAccountIdByUserId(String userId) {
        return userRepository.findById(userId)
                .map(user -> user.getAccountId() != null ? user.getAccountId().getAccountId() : "")
                .orElse("");
    }
}
