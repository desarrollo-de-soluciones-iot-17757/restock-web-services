package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DevicePersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicePersistenceRepository extends MongoRepository<DevicePersistenceEntity, String> {

    Optional<DevicePersistenceEntity> findByMacAddress(MacAddress macAddress);

    List<DevicePersistenceEntity> findAllByAccountId(AccountId accountId);

    boolean existsByMacAddress(MacAddress macAddress);
}
