package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyNames;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.SupplyCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Implementation of {@link SupplyCommandService} for handling write operations on {@link Supply}
 * entities within the resources bounded context.
 *
 * <p>Handles seeding of supply templates into the database. Supply templates serve as base categories
 * that {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply} aggregates
 * reference. This service ensures that creation of supply templates follows the required business rules.
 */
@Slf4j
@Service
public class SupplyCommandServiceImpl implements SupplyCommandService {

    private final SupplyRepository repository;

    public SupplyCommandServiceImpl(SupplyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Supply handle(SeedSuppliesCommand command) {
        log.info("Creating supply template: name='{}', category='{}'", command.name(), command.category());
        SupplyNames category = parseCategory(command.category());
        Supply supply = repository.save(Supply.builder()
                .name(command.name())
                .description(command.description())
                .category(category)
                .isPerishable(command.isPerishable())
                .build());
        log.info("Supply template created successfully — ID: {}", supply.getId());
        return supply;
    }

    @Override
    public Optional<Supply> update(String id, SeedSuppliesCommand command) {
        log.info("Updating supply template — ID: {}, name='{}'", id, command.name());
        return repository.findById(id).map(existing -> {
            existing.setName(command.name());
            existing.setDescription(command.description());
            existing.setCategory(parseCategory(command.category()));
            existing.setIsPerishable(command.isPerishable());
            Supply updated = repository.save(existing);
            log.info("Supply template updated successfully — ID: {}", id);
            return updated;
        }).or(() -> {
            log.warn("Supply template not found for update — ID: {}", id);
            return Optional.empty();
        });
    }

    @Override
    public void delete(String id) {
        log.info("Deleting supply template — ID: {}", id);
        if (repository.findById(id).isEmpty()) {
            log.warn("Supply template not found for deletion — ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supply not found: " + id);
        }
        repository.deleteById(id);
        log.info("Supply template deleted successfully — ID: {}", id);
    }

    private SupplyNames parseCategory(String category) {
        try {
            return SupplyNames.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
