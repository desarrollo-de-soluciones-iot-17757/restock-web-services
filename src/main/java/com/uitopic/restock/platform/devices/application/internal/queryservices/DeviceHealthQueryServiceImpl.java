package com.uitopic.restock.platform.devices.application.internal.queryservices;

import com.uitopic.restock.platform.devices.domain.model.aggregates.DeviceHealth;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceHealthLogsByDeviceIdQuery;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceHealthRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceHealthQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of DeviceHealthQueryService for retrieving device health records.
 */
@Service
@RequiredArgsConstructor
public class DeviceHealthQueryServiceImpl implements DeviceHealthQueryService {

    private final DeviceHealthRepository deviceHealthRepository;

    @Override
    public List<DeviceHealth> handle(GetDeviceHealthLogsByDeviceIdQuery query) {
        return deviceHealthRepository.findByDeviceId(query.deviceId());
    }
}
