package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyNames;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.SupplyCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SupplyCommandServiceImpl implements SupplyCommandService {

    private final SupplyRepository repository;

    public SupplyCommandServiceImpl(SupplyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Supply handle(SeedSuppliesCommand command) {
        SupplyNames category = parseCategory(command.category());
        return repository.save(Supply.builder()
                .name(command.name())
                .description(command.description())
                .category(category)
                .isPerishable(command.isPerishable())
                .build());
    }

    @Override
    public Optional<Supply> update(String id, SeedSuppliesCommand command) {
        return repository.findById(id).map(existing -> {
            existing.setName(command.name());
            existing.setDescription(command.description());
            existing.setCategory(parseCategory(command.category()));
            existing.setIsPerishable(command.isPerishable());
            return repository.save(existing);
        });
    }

    @Override
    public void delete(String id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supply not found: " + id);
        }
        repository.deleteById(id);
    }

    private SupplyNames parseCategory(String category) {
        try {
            return SupplyNames.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
