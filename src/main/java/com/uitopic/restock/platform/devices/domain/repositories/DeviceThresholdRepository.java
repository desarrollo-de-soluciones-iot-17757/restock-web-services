package com.uitopic.restock.platform.devices.domain.repositories;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

public interface DeviceThresholdRepository {

    DeviceThreshold save(DeviceThreshold threshold);

    Optional<DeviceThreshold> findById(String id);

    List<DeviceThreshold> findAllByAccountId(AccountId accountId);

    void deleteById(String id);
}
