package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "plans")
public class PlanPersistenceEntity {
}
