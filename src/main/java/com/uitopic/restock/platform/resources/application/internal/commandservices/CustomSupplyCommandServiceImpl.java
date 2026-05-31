package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @Override
    public CustomSupply handle(CreateCustomSupplyCommand command) {
        AccountId accountId = new AccountId(command.accountId());
        if (repository.existsByAccountIdAndName(accountId, command.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Supply with name '" + command.name() + "' already exists for this account");
        }
        Supply category = supplyRepository.findById(command.supplyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Supply template not found: " + command.supplyId()));

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
        return repository.save(cs);
    }

    @Override
    public Optional<CustomSupply> update(String id, CreateCustomSupplyCommand command) {
        return repository.findById(id).map(existing -> {
            Supply category = supplyRepository.findById(command.supplyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Supply template not found: " + command.supplyId()));
            Money unitPrice = SharedValueObjectFromStringAssembler.toMoneyFromString(command.unitPrice());
            //ImageURL imageUrl = (command.imageUrl() != null && !command.imageUrl().isBlank())
                    //? new ImageURL(command.imageUrl()) : existing.getPictureUrl();
            existing.update(command.description(), unitPrice,
                    new SupplyContent(command.supplyContent()),
                    new UnitMeasurement(command.unitMeasurement()),
                    new MinimumStock(command.minimumStock()));
            existing.setCategory(category);
            existing.setName(command.name());
            return repository.save(existing);
        });
    }

    @Override
    public void delete(String id) {
        repository.findById(id).ifPresentOrElse(cs -> {
            boolean hasStock = batchRepository.findByCustomSupplyId(id)
                    .stream().anyMatch(b -> b.getCurrentQuantity() > 0);
            if (hasStock) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Supply has active batches with stock — deplete them first");
            }
            repository.deleteById(id);
            eventPublisher.publishEvent(new CustomSupplyDeletedEvent(id, cs.getAccountId().getAccountId()));
        }, () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom supply not found: " + id); });
    }
}
