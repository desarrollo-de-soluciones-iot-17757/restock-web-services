package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;

/**
 * Implementation of {@link com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService}
 * for handling write operations on {@link CustomSupply} aggregates within the resources bounded context.
 */
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.MinimumStock;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyContent;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import com.uitopic.restock.platform.shared.interfaces.rest.transform.SharedValueObjectFromStringAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Implementation of {@link CustomSupplyCommandService} for handling write operations on
 * {@link CustomSupply} aggregates.
 *
 * <p>Orchestrates custom supply creation, updates, and deletion. On deletion, the service
 * verifies that no active batches with remaining stock exist before removing the supply,
 * then publishes a {@link com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent}
 * via Spring's {@link ApplicationEventPublisher} to notify other bounded contexts.
 */
@Slf4j
@Service
public class CustomSupplyCommandServiceImpl implements CustomSupplyCommandService {

    private final CustomSupplyRepository repository;
    private final BatchRepository batchRepository;
    private final SupplyRepository supplyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomSupplyCommandServiceImpl(CustomSupplyRepository repository,
                                          BatchRepository batchRepository,
                                          SupplyRepository supplyRepository,
                                          ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.batchRepository = batchRepository;
        this.supplyRepository = supplyRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new custom supply for the account specified in the command.
     * Validates name uniqueness within the account and resolves the referenced supply template.
     *
     * @param command the command containing all data required to create the custom supply
     * @return the newly created and persisted {@link CustomSupply} aggregate
     * @throws org.springframework.web.server.ResponseStatusException with 409 if the name already exists,
     *         or 422 if the referenced supply template is not found
     */
    @Override
    public CustomSupply handle(CreateCustomSupplyCommand command) {
        log.info("Creating custom supply '{}' for account ID: {}", command.name(), command.accountId());
        AccountId accountId = new AccountId(command.accountId());
        if (repository.existsByAccountIdAndName(accountId, command.name())) {
            log.warn("Custom supply name '{}' already exists for account ID: {}", command.name(), command.accountId());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Supply with name '" + command.name() + "' already exists for this account");
        }
        Supply category = supplyRepository.findById(command.supplyId())
                .orElseThrow(() -> {
                    log.warn("Supply template not found: {}", command.supplyId());
                    return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Supply template not found: " + command.supplyId());
                });

        Money unitPrice = SharedValueObjectFromStringAssembler.toMoneyFromString(command.unitPrice());
        //ImageURL imageUrl = (command.imageUrl() != null && !command.imageUrl().isBlank())
        //        ? new ImageURL(command.imageUrl()) : null;

        CustomSupply cs = CustomSupply.builder()
                .accountId(accountId)
                .name(command.name())
                .description(command.description())
                .category(category)
                .unitPrice(unitPrice)
                .supplyContent(new SupplyContent(command.supplyContent()))
                .unitMeasurement(new UnitMeasurement(command.unitMeasurement()))
                .minimumStock(new MinimumStock(command.minimumStock()))
                .build();
        CustomSupply saved = repository.save(cs);
        log.info("Custom supply created with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Updates an existing custom supply with the data provided in the command.
     * Replaces all mutable fields including name, category, pricing, and stock threshold.
     *
     * @param id      the unique identifier of the custom supply to update
     * @param command the command containing the updated supply data
     * @return an {@link Optional} containing the updated {@link CustomSupply}, or empty if not found
     * @throws org.springframework.web.server.ResponseStatusException with 422 if the referenced supply template is not found
     */
    @Override
    public Optional<CustomSupply> update(String id, CreateCustomSupplyCommand command) {
        log.info("Updating custom supply ID: {}", id);
        return repository.findById(id).map(existing -> {
            Supply category = supplyRepository.findById(command.supplyId())
                    .orElseThrow(() -> {
                        log.warn("Supply template not found: {}", command.supplyId());
                        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                                "Supply template not found: " + command.supplyId());
                    });
            Money unitPrice = SharedValueObjectFromStringAssembler.toMoneyFromString(command.unitPrice());
            //ImageURL imageUrl = (command.imageUrl() != null && !command.imageUrl().isBlank())
                    //? new ImageURL(command.imageUrl()) : existing.getPictureUrl();
            existing.update(command.description(), unitPrice,
                    new SupplyContent(command.supplyContent()),
                    new UnitMeasurement(command.unitMeasurement()),
                    new MinimumStock(command.minimumStock()));
            existing.setCategory(category);
            existing.setName(command.name());
            CustomSupply updated = repository.save(existing);
            log.info("Custom supply updated — ID: {}", id);
            return updated;
        }).or(() -> {
            log.warn("Custom supply not found for update: {}", id);
            return Optional.empty();
        });
    }

    /**
     * Deletes a custom supply after verifying it has no active batches with remaining stock.
     * Publishes a {@link com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent}
     * upon successful deletion.
     *
     * @param id the unique identifier of the custom supply to delete
     * @throws org.springframework.web.server.ResponseStatusException with 404 if not found,
     *         or 409 if active batches with stock exist
     */
    @Override
    public void delete(String id) {
        log.info("Deleting custom supply ID: {}", id);
        repository.findById(id).ifPresentOrElse(cs -> {
            boolean hasStock = batchRepository.findByCustomSupplyId(id)
                    .stream().anyMatch(b -> b.getCurrentStock() != null && b.getCurrentStock().stock() > 0);
            if (hasStock) {
                log.warn("Cannot delete custom supply {} — has active batches with stock", id);
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Supply has active batches with stock — deplete them first");
            }
            repository.deleteById(id);
            eventPublisher.publishEvent(new CustomSupplyDeletedEvent(id, cs.getAccountId().getAccountId()));
            log.info("Custom supply deleted successfully — ID: {}", id);
        }, () -> {
            log.warn("Custom supply not found for deletion: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom supply not found: " + id);
        });
    }
}
