package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.exception.BranchNotFoundException;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the BranchCommandService interface, responsible for handling commands related to Branch entities. This service provides methods for creating, updating, and deleting branches, as well as updating branch images. It interacts with the BranchRepository to perform database operations and uses ApplicationEventPublisher to publish events when a branch is deleted.
 */
@Service
public class BranchCommandServiceImpl implements BranchCommandService {

    /**
     * The BranchRepository used to perform CRUD operations on Branch entities. This repository is injected into the service and is responsible for saving, retrieving, and deleting Branch entities from the underlying data store.
     */
    private final BranchRepository repository;

    /**
     * The ApplicationEventPublisher used to publish events related to branch deletion. This publisher is used to notify other components that a branch has been deleted.
     */
    private final ApplicationEventPublisher eventPublisher;

    private final ImageService imageService;

    /**
     * Constructor for BranchCommandServiceImpl. This constructor takes a BranchRepository and ApplicationEventPublisher as parameters and assigns them to the repository and eventPublisher fields respectively.
     */
    public BranchCommandServiceImpl(BranchRepository repository, ApplicationEventPublisher eventPublisher, ImageService imageService) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.imageService = imageService;
    }

    /**
     * Handles the CreateBranchCommand by creating a new Branch entity and saving it to the database.
     *
     * @param command the CreateBranchCommand containing the necessary information to create a new branch
     * @return the created Branch entity
     */
    @Override
    public Branch handle(CreateBranchCommand command) {
        var accountId = new AccountId(command.accountId());
        if (repository.existsByNameAndAccountId(command.name(), accountId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch name already exists for this account");

        String photoUrl = null;
        String photoPublicId = null;

        if (command.hasNewPhoto()) {
            try {
                var uploadResult = imageService.upload(command.image(), command.photoFileName());
                photoUrl = uploadResult.get("url");
                photoPublicId = uploadResult.get("publicId");
            } catch (Exception e) {
                throw new IllegalArgumentException("Error uploading photo to storage: " + e.getMessage());
            }
        }

        Branch branch = new Branch(
                command.name(),
                command.description(),
                command.address(),
                command.city(),
                command.country(),
                command.regionOrState(),
                command.accountId(),
                photoUrl,
                photoPublicId
        );
        branch.applyImage(photoUrl, photoPublicId);

        return repository.save(branch);
    }

    /**
     * Handles the UpdateBranchInfoCommand by updating an existing Branch entity's information and saving the changes to the database. This method checks for business rules such as unique branch names within an account and unique branch locations before applying updates.
     *
     * @param command the UpdateBranchInfoCommand containing the necessary information to update an existing branch
     * @return an Optional containing the updated Branch entity if the update was successful, or empty if no Branch with the given ID exists
     */
    @Override
    public Optional<Branch> handle(UpdateBranchInfoCommand command) {
        var branch = repository.findById(command.branchId()).orElseThrow(() -> new BranchNotFoundException(command.branchId()));
        String oldPublicId = branch.hasDefaultImage() ? null : branch.getImageUrl().publicId();

        String newPhotoUrl = null;
        String newPhotoPublicId = null;

        if (command.hasNewPhoto()) {
            try {
                var uploadResult = imageService.upload(command.image(), command.photoFileName());
                newPhotoUrl = uploadResult.get("url");
                newPhotoPublicId = uploadResult.get("publicId");
            } catch (Exception e) {
                throw new IllegalArgumentException("Error uploading new photo to storage: " + e.getMessage());
            }
        }

        branch.updateBranch(command.name(), command.address(), command.city(),
                command.country(), command.regionOrState(), command.description(),
                newPhotoUrl, newPhotoPublicId);

        repository.save(branch);

        if (oldPublicId != null) {
            try {
                imageService.delete(oldPublicId);
            } catch (Exception e) {
                System.err.println("Warning: Could not delete old photo [" + oldPublicId + "]: " + e.getMessage());
            }
        }

        return Optional.of(branch);
    }

    /**
     * Handles the update of a branch's image by updating the image URL of an existing Branch entity and saving the changes to the database. This method checks if a Branch with the given ID exists, and if so, updates its image URL based on the provided image data. If the new image URL is null or blank, it sets the image URL to null.
     *
     * @param command the CreateBranchCommand containing the necessary information to update the branch's image
     * @return an Optional containing the updated Branch entity if the update was successful, or empty if no Branch with the given ID exists
     */
    @Override
    public Optional<Branch> updateImage(UpdateBranchImageCommand command) {

        var branch = repository.findById(command.branchId()).orElseThrow(() -> new BranchNotFoundException(command.branchId()));
        String oldPublicId = branch.hasDefaultImage() ? null : branch.getImageUrl().publicId();

        String newPhotoUrl = null;
        String newPhotoPublicId = null;

        if (command.hasNewPhoto()) {
            try {
                var uploadResult = imageService.upload(command.image(), command.photoFileName());
                newPhotoUrl = uploadResult.get("url");
                newPhotoPublicId = uploadResult.get("publicId");
            } catch (Exception e) {
                throw new IllegalArgumentException("Error uploading new photo to storage: " + e.getMessage());
            }
        }

        branch.applyImage(newPhotoUrl, newPhotoPublicId);
        repository.save(branch);

        if (oldPublicId != null) {
            try {
                imageService.delete(oldPublicId);
            } catch (Exception e) {
                System.err.println("Warning: Could not delete old photo [" + oldPublicId + "]: " + e.getMessage());
            }
        }

        return Optional.of(branch);
    }

    /**
     * Handles the deletion of a branch by deactivating the Branch entity and publishing a BranchDeletedEvent. This method checks if a Branch with the given ID exists, and if so, deactivates it and saves the changes to the repository. It then publishes an event to notify other components that the branch has been deleted.
     *
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
