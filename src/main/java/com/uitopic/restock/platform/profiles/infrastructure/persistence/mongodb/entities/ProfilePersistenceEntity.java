package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "profiles")
public class ProfilePersistenceEntity extends AuditableAbstractPersistenceEntity {
    private String userId;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;
    private String avatarPublicId;
    private String gender;
    private String birthDate;
}
