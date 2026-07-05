package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreatePhysicalAnomalyCommand;
import com.uitopic.restock.platform.tracking.domain.repositories.PhysicalAnomalyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of PhysicalAnomalyCommandService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PhysicalAnomalyCommandServiceImpl implements PhysicalAnomalyCommandService {

    private final PhysicalAnomalyRepository physicalAnomalyRepository;

    @Override
    public PhysicalAnomaly handle(CreatePhysicalAnomalyCommand command) {
        log.info("Handling CreatePhysicalAnomalyCommand for device: {}, value: {}", command.deviceId(), command.registeredValue());

        PhysicalAnomaly anomaly = new PhysicalAnomaly(command);
        return physicalAnomalyRepository.save(anomaly);
    }
}
