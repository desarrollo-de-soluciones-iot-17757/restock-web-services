package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.exception.BranchNotFoundException;
import com.uitopic.restock.platform.resources.domain.exception.NameAlreadyExist;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
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
 * On deletion, the branch is transitioned to {@link BranchStates#INACTIVE} (soft delete)
 * and a {@link BranchDeletedEvent} is published via Spring's {@link ApplicationEventPublisher}
 * to notify other bounded contexts.
 */
@Slf4j
@Service
public class BranchCommandServiceImpl implements BranchCommandService {

    private final BranchRepository repository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageService imageService;

    public BranchCommandServiceImpl(BranchRepository repository,
                                    ApplicationEventPublisher eventPublisher,
                                    ImageService imageService) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.imageService = imageService;
    }

    /**
     * Creates a new branch for the account specified in the command.
     * Validates that the branch name is unique within the account before persisting.
     *
     * @param command the command containing all data required to create the branch
     * @return the newly created and persisted {@link Branch} aggregate
     * @throws ResponseStatusException with 400 if the branch name already exists
     */
    @Override
    public Branch handle(CreateBranchCommand command) {
        log.info("Creating branch '{}' for account ID: {}", command.name(), command.accountId());
        var accountId = new AccountId(command.accountId());
        if (repository.existsByNameAndAccountId(command.name(), accountId)) {
            log.warn("Branch name '{}' already exists for account ID: {}", command.name(), command.accountId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch name already exists for this account");
        }

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

        ImageURL imageUrl = (photoUrl != null && !photoUrl.isBlank()) ? new ImageURL(photoUrl) : null;

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
     * Validates name uniqueness if the name changes. Replaces the photo if a new one is provided.
     *
     * @param command the command containing the branch ID and the fields to update
     * @return an {@link Optional} containing the updated {@link Branch}, or empty if not found
     * @throws NameAlreadyExist if the new name conflicts with another branch in the same account
     */
    @Override
    public Optional<Branch> handle(UpdateBranchInfoCommand command) {
        log.info("Updating branch ID: {}", command.branchId());
        var branch = repository.findById(command.branchId())
                .orElseThrow(() -> new BranchNotFoundException(command.branchId()));

        if (command.name() != null && !command.name().equals(branch.getName())) {
            if (repository.existsByNameAndAccountId(command.name(), branch.getAccountId())) {
                log.warn("Branch name '{}' already exists for account ID: {}", command.name(), branch.getAccountId());
                throw new NameAlreadyExist("Branch name already exists for this account");
            }
        }

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

        Address addr = (command.address() != null)
                ? new Address(command.address(), command.city(), command.regionOrState(), command.country())
                : null;

        branch.update(addr, command.description(), command.name());
        branch.applyImage(newPhotoUrl, newPhotoPublicId);
        repository.save(branch);

        if (oldPublicId != null) {
            try {
                imageService.delete(oldPublicId);
            } catch (Exception e) {
                log.warn("Could not delete old photo [{}]: {}", oldPublicId, e.getMessage());
            }
        }

        log.info("Branch updated — ID: {}", branch.getId());
        return Optional.of(branch);
    }

    /**
     * Updates the image of an existing branch, deleting the old one from storage if it exists.
     *
     * @param command the command containing the branch ID and new image data
     * @return an {@link Optional} containing the updated {@link Branch}, or empty if not found
     */
    @Override
    public Optional<Branch> updateImage(UpdateBranchImageCommand command) {
        log.info("Updating image for branch ID: {}", command.branchId());
        var branch = repository.findById(command.branchId())
                .orElseThrow(() -> new BranchNotFoundException(command.branchId()));

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
                log.warn("Could not delete old photo [{}]: {}", oldPublicId, e.getMessage());
            }
        }

        log.info("Branch image updated — ID: {}", branch.getId());
        return Optional.of(branch);
    }

    /**
     * Logically deletes a branch by setting its status to {@link BranchStates#INACTIVE}
     * and publishing a {@link BranchDeletedEvent}.
     *
     * @param branchId the unique identifier of the branch to deactivate
     * @throws ResponseStatusException with 404 if the branch does not exist
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