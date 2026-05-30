package com.uitopic.restock.platform.shared.domain.model.aggregates;

import lombok.Getter;
import lombok.Setter;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

/**
 * Abstract base class for auditable aggregate roots in the domain model.
 * This class provides common fields for tracking creation and modification timestamps.
 */
@Getter
@Setter
public abstract class AuditableAbstractAggregateRoot {

    // Unique identifier for the aggregate root
    @MongoId
    @BsonRepresentation(BsonType.OBJECT_ID)
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
