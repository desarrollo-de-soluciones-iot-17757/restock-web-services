package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.AccountStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "accounts")
public class AccountPersistenceEntity extends AuditableAbstractPersistenceEntity {
    private AccountId accountId;
    private String email;
    private String stripeCustomerId;
    private AccountStatus status;
    private String currentPlanId;
}
