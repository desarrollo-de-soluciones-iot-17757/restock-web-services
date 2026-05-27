package com.uitopic.restock.platform.shared.domain.model.aggregates;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * Abstract base class for auditable aggregate roots in the domain model.
 * This class provides common fields for tracking creation and modification timestamps.
 */
@Getter
@Setter
public abstract class AuditableAbstractAggregateRoot {

    // Unique identifier for the aggregate root
    @Id
    private String id;

    // Timestamp for when the aggregate root was created
    @CreatedDate
    @Field("createdAt")
    private Instant createdAt;

    // Timestamp for when the aggregate root was last modified
    @LastModifiedDate
    @Field("updatedAt")
    private Instant updatedAt;
}
