package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "subscriptions")
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {
    private AccountId accountId;
    private String planId;
    private String stripeSubscriptionId;
    private SubscriptionStatus status;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private boolean cancelAtPeriodEnd;
}
