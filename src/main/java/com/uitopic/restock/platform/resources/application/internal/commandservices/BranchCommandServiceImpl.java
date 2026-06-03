package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.DeleteBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchStatusCommand;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchCommandService;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Application service for Branch write operations.
 *
 * Handles branch creation, update and logical deletion.
 * Branch images are uploaded through the shared ImageService when provided.
 */
@Slf4j
@Service
@Transactional
public class BranchCommandServiceImpl implements BranchCommandService {

    private final BranchRepository branchRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageService imageService;

    /**
     * Creates a BranchCommandServiceImpl with the required dependencies.
     *
     * @param branchRepository repository used to persist branches
     * @param eventPublisher publisher used to emit branch domain events
     * @param imageService service used to upload and delete branch images
     */
    public BranchCommandServiceImpl(
            BranchRepository branchRepository,
            ApplicationEventPublisher eventPublisher,
            ImageService imageService
    ) {
        this.branchRepository = branchRepository;
        this.eventPublisher = eventPublisher;
        this.imageService = imageService;
    }

    /**
     * Creates a new branch.
     *
     * If the command contains an image, the image is uploaded before creating
     * the branch. If no image is provided, the Branch aggregate will apply its
     * default image.
     *
     * @param command command with the branch data
     * @return created branch
     */
    @Override
    public Branch handle(CreateBranchCommand command) {
        log.info("Creating branch '{}' for accountId='{}'", command.name(), command.accountId());

        AccountId accountId = new AccountId(command.accountId());

        if (branchRepository.existsByNameAndAccountId(command.name(), accountId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Branch name already exists for this account"
            );
        }

        String imageUrl = null;
        String imagePublicId = null;

        if (command.hasNewPhoto()) {
            try {
                var uploadResult = imageService.upload(command.image(), command.photoFileName());
                imageUrl = uploadResult.get("url");
                imagePublicId = uploadResult.get("publicId");
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Error uploading branch image: " + e.getMessage()
                );
            }
        }

        Branch branch = new Branch(
                command.name(),
                command.address(),
                command.city(),
                command.regionOrState(),
                command.country(),
                command.description(),
                command.accountId(),
                imageUrl,
                imagePublicId
        );

        Branch saved = branchRepository.save(branch);

        log.info("Branch created successfully: id='{}'", saved.getId());

        return saved;
    }

    /**
     * Updates an existing branch.
     *
     * If a new image is provided, it is uploaded and replaces the previous one.
     * If no image is provided, the current branch image is preserved.
     *
     * @param command command with the updated branch data
     * @return updated branch, or empty if not found
     */
    @Override
    public Optional<Branch> handle(UpdateBranchCommand command) {
        log.info("Updating branch id='{}'", command.branchId());

        return branchRepository.findById(command.branchId()).map(branch -> {
            boolean duplicatedName = branchRepository.existsByNameAndAccountIdAndIdNot(
                    command.name(),
                    branch.getAccountId(),
                    command.branchId()
            );

            if (duplicatedName) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Another branch with this name already exists for this account"
                );
            }

            String previousPublicId = branch.hasDefaultImage()
                    ? null
                    : branch.getImageUrl().publicId();

            String imageUrl = branch.getImageUrl() != null
                    ? branch.getImageUrl().url()
                    : null;

            String imagePublicId = branch.getImageUrl() != null
                    ? branch.getImageUrl().publicId()
                    : null;

            if (command.hasNewPhoto()) {
                try {
                    var uploadResult = imageService.upload(command.image(), command.photoFileName());
                    imageUrl = uploadResult.get("url");
                    imagePublicId = uploadResult.get("publicId");
                } catch (Exception e) {
                    throw new ResponseStatusException(
                            HttpStatus.UNPROCESSABLE_ENTITY,
                            "Error uploading branch image: " + e.getMessage()
                    );
                }
            }

            branch.update(
                    command.name(),
                    command.description(),
                    command.address(),
                    command.city(),
                    command.regionOrState(),
                    command.country(),
                    imageUrl,
                    imagePublicId
            );

            Branch updated = branchRepository.save(branch);

            if (command.hasNewPhoto() && previousPublicId != null) {
                try {
                    imageService.delete(previousPublicId);
                    log.info("Previous branch image deleted: publicId='{}'", previousPublicId);
                } catch (Exception e) {
                    log.warn(
                            "Could not delete previous branch image: publicId='{}', error='{}'",
                            previousPublicId,
                            e.getMessage()
                    );
                }
            }

            log.info("Branch updated successfully: id='{}'", updated.getId());

            return updated;
        });
    }

    /**
     * Deactivates a branch.
     *
     * This method performs a logical deletion by changing the branch status and
     * publishing a BranchDeletedEvent.
     *
     * @param command command with the branch identifier
     */
    @Override
    public void handle(DeleteBranchCommand command) {
        log.info("Deactivating branch id='{}'", command.branchId());

        Branch branch = branchRepository.findById(command.branchId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Branch not found: " + command.branchId()
                ));

        branch.deactivate();

        branchRepository.save(branch);

        eventPublisher.publishEvent(new BranchDeletedEvent(
                branch.getId(),
                branch.getAccountId().getAccountId()
        ));

        log.info("Branch deactivated successfully: id='{}'", branch.getId());
    }

    /**
     * Updates status of an existing branch.
     *
     * @param command command with the updated status branch
     * @return updated branch, or empty if not found
     */
    @Override
    public Optional<Branch> handle(UpdateBranchStatusCommand command) {
        return branchRepository.findById(command.branchId()).map(branch -> {
            branch.changeStatus(command.status());
            return branchRepository.save(branch);
        });
    }
}