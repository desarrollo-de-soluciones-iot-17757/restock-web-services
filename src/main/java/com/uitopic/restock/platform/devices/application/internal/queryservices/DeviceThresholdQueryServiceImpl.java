package com.uitopic.restock.platform.devices.application.internal.queryservices;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdsByAccountIdQuery;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdQueryService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DeviceThresholdQueryServiceImpl implements DeviceThresholdQueryService {

    private final DeviceThresholdRepository thresholdRepository;

    public DeviceThresholdQueryServiceImpl(DeviceThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    public Optional<DeviceThreshold> handle(GetDeviceThresholdByIdQuery query) {
        return thresholdRepository.findById(query.thresholdId());
    }

    @Override
    public List<DeviceThreshold> handle(GetDeviceThresholdsByAccountIdQuery query) {
        return thresholdRepository.findAllByAccountId(new AccountId(query.accountId()));
    }
}
