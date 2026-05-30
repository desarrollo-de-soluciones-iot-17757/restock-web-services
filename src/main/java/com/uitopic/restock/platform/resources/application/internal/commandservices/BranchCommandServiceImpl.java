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
        var accountId = new AccountId(command.accountId());
        if (repository.existsByNameAndAccountId(command.name(), accountId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch name already exists for this account");

        ImageURL imageUrl = (command.imageUrl() != null && !command.imageUrl().isBlank())
                ? new ImageURL(command.imageUrl()) : null;

        Branch branch = Branch.builder()
                .accountId(accountId)
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
            // Check name uniqueness only when the name is actually changing
            if (command.name() != null && !command.name().equals(branch.getName())) {
                if (repository.existsByNameAndAccountId(command.name(), branch.getAccountId()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch name already exists for this account");
            }
            branch.update(command.address(), command.city(), command.country(), command.description(), command.name());
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
        var branch = repository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: " + branchId));
        branch.deactivate();
        repository.save(branch);
        eventPublisher.publishEvent(new BranchDeletedEvent(branchId, branch.getAccountId().getAccountId()));
    }
}
