package com.uitopic.restock.platform.iam.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.subscriptions.interfaces.acl.SubscriptionsContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalSubscriptionsService {

    private final SubscriptionsContextFacade subscriptionsContextFacade;

    public ExternalSubscriptionsService(SubscriptionsContextFacade subscriptionsContextFacade) {
        this.subscriptionsContextFacade = subscriptionsContextFacade;
    }

    public String createAccountForNewUser(String accountId, String email) {
        var subscriptionAccountId = subscriptionsContextFacade.createAccount(accountId, email);
        return subscriptionAccountId != null ? subscriptionAccountId : "";
    }
}
