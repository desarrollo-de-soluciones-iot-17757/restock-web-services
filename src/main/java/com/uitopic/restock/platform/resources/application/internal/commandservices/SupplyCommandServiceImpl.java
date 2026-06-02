package com.uitopic.restock.platform.resources.application.internal.commandservices;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.services.SupplyCommandService;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Application service for Supply write operations.
 *
 * Handles the seeding of the base supply catalog from a JSON file.
 */
@Slf4j
@Service
public class SupplyCommandServiceImpl implements SupplyCommandService {

    private final SupplyRepository supplyRepository;
    private final ObjectMapper objectMapper;

    public SupplyCommandServiceImpl(SupplyRepository supplyRepository, ObjectMapper objectMapper) {
        this.supplyRepository = supplyRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Seeds the base supply catalog from the supplies.json file.
     *
     * @param command command to start the seed process
     */
    @Override
    public void handle(SeedSuppliesCommand command) {
        log.info("Starting supplies seed process");

        try {
            InputStream inputStream = new ClassPathResource("jsonData/supplies.json").getInputStream();

            List<SupplySeedData> supplies = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {}
            );

            for (SupplySeedData supplyData : supplies) {
                if (supplyRepository.existsByName(supplyData.name())) {
                    log.debug("Supply already exists: {}", supplyData.name());
                    continue;
                }

                Supply supply = new Supply(
                        supplyData.name(),
                        supplyData.description(),
                        supplyData.category(),
                        supplyData.isPerishable()
                );

                supplyRepository.save(supply);

                log.info("Seeded supply: {}", supply.getName());
            }

            log.info("Supplies seed process finished successfully");

        } catch (Exception e) {
            throw new RuntimeException("Error loading supplies.json", e);
        }
    }

    /**
     * Internal DTO used only for reading seed data from supplies.json.
     *
     * @param name supply name
     * @param description supply description
     * @param category supply category
     * @param isPerishable whether the supply is perishable
     */
    private record SupplySeedData(
            String name,
            String description,
            String category,
            Boolean isPerishable
    ) {
    }
}