package com.uitopic.restock.platform.shared.domain.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * Base class for auditable entities, providing common fields for tracking creation and modification timestamps.
 */
@Getter
@Setter
public abstract class AuditableModel {

    // Unique identifier for the entity
    @Id
    private String id;

    // Timestamp for when the entity was created
    @CreatedDate
    @Field("createdAt")
    private Instant createdAt;

    // Timestamp for when the entity was last modified
    @LastModifiedDate
    @Field("updatedAt")
    private Instant updatedAt;
}
