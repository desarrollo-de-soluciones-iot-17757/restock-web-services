package com.uitopic.restock.platform.devices.application.acl;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByDeviceIdQuery;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdQueryService;
import com.uitopic.restock.platform.devices.interfaces.acl.DevicesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * DevicesContextFacadeImpl is an implementation of the DevicesContextFacade interface. It serves as a facade to provide a simplified interface for accessing device-related information, specifically the anomaly threshold for a given device ID.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DevicesContextFacadeImpl implements DevicesContextFacade {

    // The DeviceThresholdQueryService is injected to handle queries related to device thresholds.
    private final DeviceThresholdQueryService deviceThresholdQueryService;

    // The DeviceRepository is injected to access device data, although it is not currently used in the implemented methods.
    private final DeviceRepository deviceRepository;

    /**
     * @inheritDocs
     */
    @Override
    public Boolean existsByDeviceId(DeviceId deviceId) {
        return deviceRepository.existsByDeviceId(deviceId.getDeviceId());
    }

    /**
     * @inheritDocs
     */
    @Override
    public Pair<Double, AccountId> getAnomalyThresholdByDeviceId(DeviceId deviceId) {
        var getDeviceThresholdByDeviceIdQuery = new GetDeviceThresholdByDeviceIdQuery(deviceId);
        var deviceThreshold = deviceThresholdQueryService.handle(getDeviceThresholdByDeviceIdQuery);
        return Pair.of(
                deviceThreshold.map(DeviceThreshold::getAnomalyThreshold).orElse(null),
                deviceThreshold.map(DeviceThreshold::getAccountId).orElse(null)
        );
    }
}
