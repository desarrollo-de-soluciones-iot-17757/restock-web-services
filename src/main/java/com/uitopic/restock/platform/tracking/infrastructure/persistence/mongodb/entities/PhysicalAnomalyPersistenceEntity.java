package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Persistence entity representing a physical anomaly document in MongoDB.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "physical_anomalies")
public class PhysicalAnomalyPersistenceEntity {

    @Id
    private String id;
    private String deviceId;
    private Double registeredValue;
    private Instant timestamp;
}
