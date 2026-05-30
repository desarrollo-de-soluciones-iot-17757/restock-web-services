package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.exception.NameAlreadyExist;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Implementation of the BranchCommandService interface, responsible for handling commands related to Branch entities. This service provides methods for creating, updating, and deleting branches, as well as updating branch images. It interacts with the BranchRepository to perform database operations and uses ApplicationEventPublisher to publish events when a branch is deleted.
 */
@Service
public class BranchCommandServiceImpl implements BranchCommandService {

    /** The BranchRepository used to perform CRUD operations on Branch entities. This repository is injected into the service and is responsible for saving, retrieving, and deleting Branch entities from the underlying data store. */
    private final BranchRepository repository;

    /** The ApplicationEventPublisher used to publish events related to branch deletion. This publisher is used to notify other components that a branch has been deleted. */
    private final ApplicationEventPublisher eventPublisher;

    /** Constructor for BranchCommandServiceImpl. This constructor takes a BranchRepository and ApplicationEventPublisher as parameters and assigns them to the repository and eventPublisher fields respectively. */
    public BranchCommandServiceImpl(BranchRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Handles the CreateBranchCommand by creating a new Branch entity and saving it to the database.
     * @param command the CreateBranchCommand containing the necessary information to create a new branch
     * @return the created Branch entity
     */
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
                .location(new Address(command.address(), command.city(), command.regionOrState(), command.country()))
                .status(BranchStates.ACTIVE)
                .description(command.description())
                .build();
        return repository.save(branch);
    }

    /** Handles the UpdateBranchInfoCommand by updating an existing Branch entity's information and saving the changes to the database. This method checks for business rules such as unique branch names within an account and unique branch locations before applying updates.
     * @param command the UpdateBranchInfoCommand containing the necessary information to update an existing branch
     * @return an Optional containing the updated Branch entity if the update was successful, or empty if no Branch with the given ID exists
     */
    @Override
    public Optional<Branch> handle(UpdateBranchInfoCommand command) {
        return repository.findById(command.branchId()).map(branch -> {

            if (command.name() != null && !command.name().equals(branch.getName())) {
                if (repository.existsByNameAndAccountId(command.name(), branch.getAccountId()))
                    throw new NameAlreadyExist("Branch name already exists for this account");
            }

            branch.update(command.address(), command.city(), command.regionOrState(), command.country(), command.description(), command.name());
            return repository.save(branch);
        });
    }

    /** Handles the update of a branch's image by updating the image URL of an existing Branch entity and saving the changes to the database.
     * @param branchId the unique identifier of the branch to update
     * @param imageUrl the new image URL to set for the branch
     * @return an Optional containing the updated Branch entity if the update was successful, or empty if no Branch with the given ID exists
     */
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

    /**
     * Handles the deletion of a branch by deactivating the Branch entity and publishing a BranchDeletedEvent. This method checks if a Branch with the given ID exists, and if so, deactivates it and saves the changes to the repository. It then publishes an event to notify other components that the branch has been deleted.
     * @param branchId the unique identifier of the branch to delete
     */
    @Override
    public void delete(String branchId) {
        var branch = repository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: " + branchId));
        branch.deactivate();
        repository.save(branch);
        eventPublisher.publishEvent(new BranchDeletedEvent(branchId, branch.getAccountId().getAccountId()));
    }
}
