package com.uitopic.restock.platform.communications.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.iam.interfaces.acl.IamContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for interacting with the IAM context to fetch user information based on account IDs.
 * This service acts as an outbound service for the Communications context, allowing it to retrieve usernames associated with a given account ID without exposing the internal workings of the IAM context. It uses the Iam
 */
@Service(value = "communicationsExternalIAMService")
@RequiredArgsConstructor
public class ExternalIAMService {

    /** Facade for interacting with the IAM context, allowing this service to fetch user information without exposing IAM internals. */
    private final IamContextFacade iamContextFacade;

    /**
     * Fetch the emails of the users associated with the given account ID.
     *
     * @param accountId the ID of the account for which to fetch associated usernames
     * @return a list of usernames associated with the given account ID, or an empty list if no users are found
     */
    List<String> getUsernamesByAccountId(AccountId accountId) {
        return iamContextFacade.getUsernamesByAccountId(accountId);
    }
}
