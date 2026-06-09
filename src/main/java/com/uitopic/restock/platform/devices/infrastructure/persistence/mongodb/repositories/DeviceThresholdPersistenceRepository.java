package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceThresholdPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceThresholdPersistenceRepository extends MongoRepository<DeviceThresholdPersistenceEntity, String> {

    List<DeviceThresholdPersistenceEntity> findAllByAccountId(AccountId accountId);
}
