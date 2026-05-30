package com.uitopic.restock.platform.iam.domain.model.aggregates;

import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Role;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "users")
public class User extends AuditableAbstractAggregateRoot {

    @Indexed(unique = true)
    private Email email;
    private String passwordHash;
    private Role role;
    private AccountId accountId;

    public User(Email email, String passwordHash, Role role, AccountId accountId) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.accountId = accountId;
    }

    public void update(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void update(Email email) {
        this.email = email;
    }

    public void update(Role role) {
        this.role = role;
    }
}
