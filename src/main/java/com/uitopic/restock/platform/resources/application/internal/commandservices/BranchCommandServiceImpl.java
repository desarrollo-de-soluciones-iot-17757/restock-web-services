package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.exception.NameAlreadyExist;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Implementation of {@link BranchCommandService} for handling write operations on {@link Branch} aggregates.
 *
 * <p>Orchestrates branch creation, updates, image changes, and logical deletion.
 * On deletion, the branch is transitioned to
 * {@link com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates#INACTIVE}
 * (soft delete) and a {@link com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent}
 * is published via Spring's {@link ApplicationEventPublisher} to notify other bounded contexts.
 */
@Slf4j
@Service
public class BranchCommandServiceImpl implements BranchCommandService {

    private final BranchRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public BranchCommandServiceImpl(BranchRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new branch for the account specified in the command.
     * Validates that the branch name is unique within the account before persisting.
     *
     * @param command the command containing all data required to create the branch
     * @return the newly created and persisted {@link Branch} aggregate
     * @throws org.springframework.web.server.ResponseStatusException with 400 if the branch name already exists
     */
    @Override
    public Branch handle(CreateBranchCommand command) {
        log.info("Creating branch '{}' for account ID: {}", command.name(), command.accountId());
        var accountId = new AccountId(command.accountId());
        if (repository.existsByNameAndAccountId(command.name(), accountId)) {
            log.warn("Branch name '{}' already exists for account ID: {}", command.name(), command.accountId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch name already exists for this account");
        }

        ImageURL imageUrl = (command.imageUrl() != null && !command.imageUrl().isBlank())
                ? new ImageURL(command.imageUrl()) : null;

        Branch branch = Branch.builder()
                .accountId(accountId)
                .name(command.name())
                .location(new Address(command.address(), command.city(), command.regionOrState(), command.country()))
                .imageUrl(imageUrl)
                .status(BranchStates.ACTIVE)
                .description(command.description())
                .build();
        Branch saved = repository.save(branch);
        log.info("Branch created with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Applies a partial or full update to an existing branch.
     * Only non-null fields in the command are applied. Validates name uniqueness if the name changes.
     *
     * @param command the command containing the branch ID and the fields to update
     * @return an {@link Optional} containing the updated {@link Branch}, or empty if not found
     * @throws com.uitopic.restock.platform.resources.domain.exception.NameAlreadyExist if the new name conflicts with another branch in the same account
     */
    @Override
    public Optional<Branch> handle(UpdateBranchInfoCommand command) {
        log.info("Updating branch ID: {}", command.branchId());
        return repository.findById(command.branchId()).map(branch -> {
            if (command.name() != null && !command.name().equals(branch.getName())) {
                if (repository.existsByNameAndAccountId(command.name(), branch.getAccountId())) {
                    log.warn("Branch name '{}' already exists for account ID: {}", command.name(), branch.getAccountId());
                    throw new NameAlreadyExist("Branch name already exists for this account");
                }
            }
            Address addr = (command.address() != null)
                    ? new Address(command.address(), command.city(), command.regionOrState(), command.country())
                    : null;
            branch.update(addr, command.description(), command.name());
            Branch updated = repository.save(branch);
            log.info("Branch updated — ID: {}", updated.getId());
            return updated;
        });
    }

    /**
     * Updates the image URL of an existing branch.
     *
     * @param branchId the unique identifier of the branch to update
     * @param imageUrl the new image URL, or {@code null} to clear the existing image
     * @return an {@link Optional} containing the updated {@link Branch}, or empty if not found
     */
    @Override
    public Optional<Branch> updateImage(String branchId, String imageUrl) {
        log.info("Updating image for branch ID: {}", branchId);
        return repository.findById(branchId).map(branch -> {
            branch.setImageUrl((imageUrl != null && !imageUrl.isBlank()) ? new ImageURL(imageUrl) : null);
            Branch updated = repository.save(branch);
            log.info("Branch image updated — ID: {}", updated.getId());
            return updated;
        });
    }

    /**
     * Logically deletes a branch by setting its status to
     * {@link com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates#INACTIVE}
     * and publishing a {@link com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent}.
     *
     * @param branchId the unique identifier of the branch to deactivate
     * @throws org.springframework.web.server.ResponseStatusException with 404 if the branch does not exist
     */
    @Override
    public void delete(String branchId) {
        log.info("Deleting (logical) branch ID: {}", branchId);
        var branch = repository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: " + branchId));
        branch.deactivate();
        repository.save(branch);
        eventPublisher.publishEvent(new BranchDeletedEvent(branchId, branch.getAccountId().getAccountId()));
        log.info("Branch deactivated and event published — ID: {}", branchId);
    }
}
