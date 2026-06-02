package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * Base JPA persistence entity for all persistence entities that require auditing.
 *
 * <p>Provides {@code id}, {@code createdAt}, and {@code updatedAt} fields.
 * This class intentionally lives in the infrastructure layer to keep JPA and
 * Spring Data auditing concerns out of the domain model.</p>
 *
 * <p>All bounded-context JPA persistence entities should extend this class
 * instead of placing {@code @Id} and auditing fields directly.</p>
 */
@Getter
public abstract class AuditableAbstractPersistenceEntity {

    @MongoId
    private String id;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    /**
     * Sets the id. Used by assemblers when reconstructing a persistence entity
     * from an existing domain object that already carries an identity.
     *
     * @param id the persistence identity to assign
     */
    public void setId(String id) {
        this.id = id;
    }
}
