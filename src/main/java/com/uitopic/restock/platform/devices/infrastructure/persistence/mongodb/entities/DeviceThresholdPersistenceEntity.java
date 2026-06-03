package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "device_thresholds")
public class DeviceThresholdPersistenceEntity {
}
