package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.Payment;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "subscriptions")
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Indexed(unique = true)
    private String businessId;
    private SubscribedPlanSnapshot planSnapshot;
    private SubscriptionsStatus status;
    private BillingCycle billingCycle;
    private boolean cancelAtPeriodEnd;
    private StripeReference stripeReference;
    private List<Payment> payments = new ArrayList<>();
    private List<String> processedStripeEventIds = new ArrayList<>();
}
