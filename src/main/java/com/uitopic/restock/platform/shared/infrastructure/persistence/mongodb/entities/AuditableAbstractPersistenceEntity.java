package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * Base Spring Data MongoDB persistence entity for all persistence entities that require auditing.
 *
 * <p>Provides {@code id}, {@code createdAt}, and {@code updatedAt} fields.
 * This class intentionally lives in the infrastructure layer to keep JPA and
 * Spring Data auditing concerns out of the domain model.</p>
 *
 * <p>All bounded-context MongoDB persistence entities should extend this class
 * instead of placing {@code @Id} and auditing fields directly.</p>
 */
@Getter
public abstract class AuditableAbstractPersistenceEntity {

    @MongoId(FieldType.STRING)
    @Setter
    private String id;

    @CreatedDate
    @Field
    private Date createdAt;

    @LastModifiedDate
    @Field
    private Date updatedAt;
}
