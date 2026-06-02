package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.DeleteCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyRepository;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Application service for CustomSupply write operations.
 *
 * Handles custom supply creation, update and deletion.
 * If an image is provided, it is uploaded through the shared ImageService and
 * stored as an ImageURL value object in the aggregate.
 */
@Slf4j
@Service
@Transactional
public class CustomSupplyCommandServiceImpl implements CustomSupplyCommandService {

    private final CustomSupplyRepository customSupplyRepository;
    private final SupplyRepository supplyRepository;
    private final ImageService imageService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates a CustomSupplyCommandServiceImpl with the required dependencies.
     *
     * @param customSupplyRepository repository used to persist custom supplies
     * @param supplyRepository repository used to find base supplies
     * @param imageService service used to upload and delete custom supply images
     * @param eventPublisher publisher used to emit custom supply events
     */
    public CustomSupplyCommandServiceImpl(
            CustomSupplyRepository customSupplyRepository,
            SupplyRepository supplyRepository,
            ImageService imageService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.customSupplyRepository = customSupplyRepository;
        this.supplyRepository = supplyRepository;
        this.imageService = imageService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new custom supply.
     *
     * @param command command with the custom supply data
     * @return created custom supply
     */
    @Override
    public CustomSupply handle(CreateCustomSupplyCommand command) {
        log.info("Creating custom supply: name='{}', accountId='{}'", command.name(), command.accountId());

        AccountId accountId = new AccountId(command.accountId());


        if (customSupplyRepository.existsByAccountIdAndName(accountId, command.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Custom supply with name '" + command.name() + "' already exists for this account"
            );
        }

        if (customSupplyRepository.existsByAccountIdAndSupplyId(accountId, command.supplyId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This supply is already registered as a custom supply for this account"
            );
        }

        Supply supply = findSupplyOrThrow(command.supplyId());

        ImageURL pictureUrl = uploadImageIfPresent(
                command.image(),
                command.photoFileName()
        );

        CustomSupply customSupply = new CustomSupply(command, supply, pictureUrl);

        CustomSupply savedCustomSupply = customSupplyRepository.save(customSupply);

        log.info("Custom supply created successfully: id='{}'", savedCustomSupply.getId());

        return savedCustomSupply;
    }

    /**
     * Updates an existing custom supply.
     *
     * If a new image is provided, it replaces the previous one. If no image is
     * provided, the current image is preserved.
     *
     * @param command command with the updated custom supply data
     * @return updated custom supply, or empty if not found
     */
    @Override
    public Optional<CustomSupply> handle(UpdateCustomSupplyCommand command) {
        log.info("Updating custom supply: id='{}'", command.customSupplyId());

        return customSupplyRepository.findById(command.customSupplyId()).map(existing -> {
            if (command.name() != null && !command.name().isBlank()) {
                boolean duplicatedName = customSupplyRepository.existsByAccountIdAndNameAndIdNot(
                        existing.getAccountId(),
                        command.name(),
                        command.customSupplyId()
                );

                if (duplicatedName) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Another custom supply with name '" + command.name() + "' already exists for this account"
                    );
                }
            }

            String oldPublicId = existing.getPictureUrl() != null
                    ? existing.getPictureUrl().publicId()
                    : null;

            ImageURL pictureUrl = existing.getPictureUrl();

            if (command.hasNewPhoto()) {
                pictureUrl = uploadImageIfPresent(
                        command.image(),
                        command.photoFileName()
                );
            }

            existing.update(command, pictureUrl);

            CustomSupply updatedCustomSupply = customSupplyRepository.save(existing);

            if (command.hasNewPhoto() && oldPublicId != null) {
                deleteOldImage(oldPublicId);
            }

            log.info("Custom supply updated successfully: id='{}'", updatedCustomSupply.getId());

            return updatedCustomSupply;
        });
    }

    /**
     * Deletes a custom supply.
     *
     * @param command command with the custom supply identifier
     */
    @Override
    public void handle(DeleteCustomSupplyCommand command) {
        log.info("Deleting custom supply: id='{}'", command.customSupplyId());

        CustomSupply customSupply = customSupplyRepository.findById(command.customSupplyId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Custom supply not found: " + command.customSupplyId()
                ));

        customSupplyRepository.deleteById(command.customSupplyId());

        eventPublisher.publishEvent(new CustomSupplyDeletedEvent(
                customSupply.getId(),
                customSupply.getAccountId().getAccountId()
        ));

        log.info("Custom supply deleted successfully: id='{}'", command.customSupplyId());
    }

    /**
     * Finds a base supply by ID.
     *
     * @param supplyId supply identifier
     * @return found supply
     */
    private Supply findSupplyOrThrow(String supplyId) {
        return supplyRepository.findById(supplyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Supply not found: " + supplyId
                ));
    }

    /**
     * Uploads an image if image data is present.
     *
     * @param image image bytes
     * @param photoFileName original file name
     * @return uploaded image URL value object, or null if no image was provided
     */
    private ImageURL uploadImageIfPresent(byte[] image, String photoFileName) {
        if (image == null || image.length == 0) {
            return null;
        }

        if (photoFileName == null || photoFileName.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Image file name cannot be null or blank"
            );
        }

        try {
            var uploadResult = imageService.upload(image, photoFileName);

            return new ImageURL(
                    uploadResult.get("url"),
                    uploadResult.get("publicId")
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Error uploading custom supply image: " + e.getMessage()
            );
        }
    }

    /**
     * Deletes an old image from storage.
     *
     * @param publicId image public ID
     */
    private void deleteOldImage(String publicId) {
        try {
            imageService.delete(publicId);
            log.info("Previous custom supply image deleted: publicId='{}'", publicId);
        } catch (Exception e) {
            log.warn(
                    "Could not delete previous custom supply image: publicId='{}', error='{}'",
                    publicId,
                    e.getMessage()
            );
        }
    }
}