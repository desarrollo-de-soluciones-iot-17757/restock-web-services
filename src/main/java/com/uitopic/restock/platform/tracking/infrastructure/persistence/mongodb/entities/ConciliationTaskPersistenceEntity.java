package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "conciliation_tasks")
public class ConciliationTaskPersistenceEntity extends AuditableAbstractPersistenceEntity {


}
