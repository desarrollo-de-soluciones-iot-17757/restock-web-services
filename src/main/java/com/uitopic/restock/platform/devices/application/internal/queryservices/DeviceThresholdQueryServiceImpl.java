package com.uitopic.restock.platform.devices.application.internal.queryservices;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByDeviceIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdsByAccountIdQuery;
import com.uitopic.restock.platform.devices.domain.repositories.DeviceThresholdRepository;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdQueryService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The DeviceThresholdQueryServiceImpl class is an implementation of the DeviceThresholdQueryService interface.
 * It provides methods to handle queries related to device thresholds, such as retrieving a threshold by its ID, retrieving thresholds by device ID, and retrieving thresholds by account ID. The class uses a repository to access the underlying data store and is annotated with @Service to indicate that it is a Spring service component. The @Slf4j annotation is used to enable logging within the class, and the @RequiredArgsConstructor annotation from Lombok generates a constructor for the final fields, allowing for dependency injection of the repository.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceThresholdQueryServiceImpl implements DeviceThresholdQueryService {

    // The repository is injected via constructor injection, which is facilitated by the @RequiredArgsConstructor annotation from Lombok.
    private final DeviceThresholdRepository thresholdRepository;

    /**
     * @inheritDocs
     */
    @Override
    public Optional<DeviceThreshold> handle(GetDeviceThresholdByIdQuery query) {
        return thresholdRepository.findById(query.thresholdId());
    }

    /**
     * @inheritDocs
     */
    @Override
    public Optional<DeviceThreshold> handle(GetDeviceThresholdByDeviceIdQuery query) {
        return thresholdRepository.findByDeviceId(query.deviceId());
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<DeviceThreshold> handle(GetDeviceThresholdsByAccountIdQuery query) {
        return thresholdRepository.findAllByAccountId(new AccountId(query.accountId()));
    }
}
