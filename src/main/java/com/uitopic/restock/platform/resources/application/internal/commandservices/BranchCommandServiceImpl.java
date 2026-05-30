package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BranchCommandServiceImpl implements BranchCommandService {

    private final BranchRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public BranchCommandServiceImpl(BranchRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Branch handle(CreateBranchCommand command) {
        ImageURL imageUrl = null;
        if (command.imageUrl() != null && !command.imageUrl().isBlank()) {
            imageUrl = new ImageURL(command.imageUrl());
        }
        Branch branch = Branch.builder()
                .accountId(new AccountId(command.accountId()))
                .name(command.name())
                .address(command.address())
                .city(command.city())
                .country(command.country())
                .imageUrl(imageUrl)
                .status("active")
                .description(command.description())
                .build();
        return repository.save(branch);
    }

    @Override
    public Optional<Branch> handle(UpdateBranchInfoCommand command) {
        return repository.findById(command.branchId()).map(branch -> {
            branch.setName(command.name());
            branch.setAddress(command.address());
            branch.setCity(command.city());
            branch.setCountry(command.country());
            branch.setDescription(command.description());
            return repository.save(branch);
        });
    }

    @Override
    public Optional<Branch> updateImage(String branchId, String imageUrl) {
        return repository.findById(branchId).map(branch -> {
            if (imageUrl != null && !imageUrl.isBlank()) {
                branch.setImageUrl(new ImageURL(imageUrl));
            } else {
                branch.setImageUrl(null);
            }
            return repository.save(branch);
        });
    }

    @Override
    public void delete(String branchId) {
        repository.findById(branchId).ifPresentOrElse(branch -> {
            repository.deleteById(branchId);
            eventPublisher.publishEvent(new BranchDeletedEvent(branchId, branch.getAccountId().getAccountId()));
        }, () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: " + branchId); });
    }
}
