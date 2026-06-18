package com.uitopic.restock.platform.devices.application.internal.commandservices;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.commands.*;
import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.WeightMeasurement;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceRepository;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring.SpringDomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;
    private final DeviceThresholdRepository deviceThresholdRepository;
    private final SpringDomainEventPublisher eventPublisher;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository, SpringDomainEventPublisher eventPublisher, DeviceThresholdRepository deviceThresholdRepository) {
        this.deviceRepository = deviceRepository;
        this.eventPublisher = eventPublisher;
        this.deviceThresholdRepository = deviceThresholdRepository;
    }

    @Override
    public Device handle(RegisterDeviceCommand command) {
        log.info("Registering device with MAC address='{}'", command.macAddress());

        if (deviceRepository.existsByMacAddress(new MacAddress(command.macAddress()))) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Device with MAC address '" + command.macAddress() + "' already registered"
            );
        }

        var device = new Device(command.macAddress(), command.accountId(), command.description());
        var saved = deviceRepository.save(device);
        publishDeviceEvents(device);
        log.info("Device registered successfully: id='{}'", saved.getId());
        return saved;
    }

    @Override
    public Optional<Device> handle(AddDeviceSpecificationsCommand command) {
        log.info("Adding specifications to device id='{}'", command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            device.addSpecifications(command.manufacturer(), command.model(), command.firmwareVersion());
            var saved = deviceRepository.save(device);
            log.info("Specifications added to device id='{}'", saved.getId());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(AssignDeviceToBranchCommand command) {
        log.info("Assigning device id='{}' to branch id='{}'", command.deviceId(), command.branchId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            device.assignBranch(command.branchId());
            var saved = deviceRepository.save(device);
            log.info("Device id='{}' assigned to branch id='{}'", saved.getId(), command.branchId());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(AssignBatchToDeviceCommand command) {
        log.info("Assigning batch id='{}' to device id='{}'", command.batchId(), command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            device.assignBatch(command.batchId());
            findThresholdFor(device).ifPresent(device::confirmConfiguration);
            var saved = deviceRepository.save(device);
            publishDeviceEvents(device);
            log.info("Batch id='{}' assigned to device id='{}'", command.batchId(), saved.getId());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(AssignSupplyThresholdCommand command) {
        log.info("Assigning threshold id='{}' to device id='{}'", command.supplyThresholdId(), command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            device.assignSupplyThreshold(command.supplyThresholdId());
            findThresholdFor(device).ifPresent(device::confirmConfiguration);
            var saved = deviceRepository.save(device);
            publishDeviceEvents(device);
            log.info("Threshold id='{}' assigned to device id='{}'", command.supplyThresholdId(), saved.getId());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(UpdateDeviceMeasurementCommand command) {
        log.info("Updating measurement for device id='{}'", command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            var weightUnit = command.weightUnitAbbreviation() != null
                    ? new UnitMeasurement(command.weightUnitName(), command.weightUnitAbbreviation())
                    : new UnitMeasurement(command.weightUnitName());

            var measurement = new WeightMeasurement(
                    command.unitStockWeight(),
                    command.tareWeight(),
                    command.grossWeight(),
                    command.calibrationDate(),
                    weightUnit
            );
            device.updateMeasurement(measurement);
            var deviceThreshold = findThresholdFor(device)
                    .orElseThrow(() -> new IllegalStateException("Device must be CONFIGURED before calibration"));
            device.confirmCalibration(deviceThreshold);
            var saved = deviceRepository.save(device);
            publishDeviceEvents(device);
            log.info("Measurement updated for device id='{}'", saved.getId());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(ConfirmDeviceConfigurationCommand command) {
        log.info("Confirming calibration for device id='{}'", command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            var deviceThreshold = findThresholdFor(device)
                    .orElseThrow(() -> new IllegalArgumentException("Supply threshold not found: " + command.deviceId()));
            device.confirmCalibration(deviceThreshold);
            var saved = deviceRepository.save(device);
            publishDeviceEvents(device);
            log.info("Calibration confirmed for device id='{}', status='{}'", saved.getId(), saved.getStatus());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(UpdateJustifiedWithdrawnStockCommand command) {
        log.info("Updating justified withdrawn stock for device id='{}'", command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            device.updateJustifiedWithdrawnStock(command.amount());
            var saved = deviceRepository.save(device);
            log.info("Justified withdrawn stock updated for device id='{}'", saved.getId());
            return saved;
        });
    }

    @Override
    public Optional<Device> handle(DeactivateDeviceCommand command) {
        log.info("Deactivating device id='{}'", command.deviceId());

        return deviceRepository.findById(command.deviceId()).map(device -> {
            device.deactivate();
            var saved = deviceRepository.save(device);
            log.info("Device id='{}' deactivated", saved.getId());
            return saved;
        });
    }

    private Optional<DeviceThreshold> findThresholdFor(Device device) {
        if (device.getId() == null || device.getAssignedBatchId() == null || device.getSupplyThresholdId() == null) {
            return Optional.empty();
        }
        return deviceThresholdRepository.findByDeviceId(new DeviceId(device.getId()))
                .filter(threshold -> device.getSupplyThresholdId().equals(threshold.getId()));
    }

    private void publishDeviceEvents(Device device) {
        device.domainEvents().forEach(eventPublisher::publish);
        device.clearDomainEvents();
    }
}
