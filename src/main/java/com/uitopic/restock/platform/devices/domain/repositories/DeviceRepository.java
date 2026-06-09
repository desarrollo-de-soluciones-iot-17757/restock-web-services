package com.uitopic.restock.platform.devices.domain.repositories;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(String id);

    Optional<Device> findByMacAddress(MacAddress macAddress);

    List<Device> findAllByAccountId(AccountId accountId);

    void deleteById(String id);

    boolean existsByMacAddress(MacAddress macAddress);
}
