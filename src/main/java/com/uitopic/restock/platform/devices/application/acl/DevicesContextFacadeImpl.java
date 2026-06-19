package com.uitopic.restock.platform.devices.application.acl;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByDeviceIdQuery;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdQueryService;
import com.uitopic.restock.platform.devices.interfaces.acl.DevicesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring.SpringDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SpringDomainEventPublisher eventPublisher;

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

    @Override
    public Pair<Double, AccountId> getJustifiedWithdrawnStockByDeviceId(DeviceId deviceId) {
        return deviceRepository.findById(deviceId.getDeviceId())
                .map(device -> Pair.of(
                        device.getJustifiedWithdrawnStock() != null ? device.getJustifiedWithdrawnStock() : 0.0,
                        device.getAccountId()))
                .orElse(Pair.of(0.0, null));
    }

    @Override
    @Transactional
    public void updateJustifiedWithdrawnStock(DeviceId deviceId, Double amount) {
        var device = deviceRepository.findById(deviceId.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId.getDeviceId()));
        device.updateJustifiedWithdrawnStock(amount);
        deviceRepository.save(device);
    }

    @Override
    @Transactional
    public void recalibrateDevice(DeviceId deviceId) {
        var device = deviceRepository.findById(deviceId.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId.getDeviceId()));

        var deviceThreshold = deviceThresholdQueryService.handle(new GetDeviceThresholdByDeviceIdQuery(deviceId))
                .filter(threshold -> device.getSupplyThresholdId() != null && device.getSupplyThresholdId().equals(threshold.getId()))
                .orElseThrow(() -> new IllegalStateException("Device threshold not found for device: " + deviceId.getDeviceId()));

        device.confirmCalibration(deviceThreshold);
        deviceRepository.save(device);
        device.domainEvents().forEach(eventPublisher::publish);
        device.clearDomainEvents();
    }
}
