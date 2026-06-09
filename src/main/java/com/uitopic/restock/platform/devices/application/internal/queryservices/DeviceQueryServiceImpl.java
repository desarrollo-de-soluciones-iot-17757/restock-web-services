package com.uitopic.restock.platform.devices.application.internal.queryservices;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.queries.*;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceQueryService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.deviceId());
    }

    @Override
    public Optional<Device> handle(GetDeviceByMacAddressQuery query) {
        return deviceRepository.findByMacAddress(new MacAddress(query.macAddress()));
    }

    @Override
    public List<Device> handle(GetDevicesByAccountIdQuery query) {
        return deviceRepository.findAllByAccountId(new AccountId(query.accountId()));
    }
}
