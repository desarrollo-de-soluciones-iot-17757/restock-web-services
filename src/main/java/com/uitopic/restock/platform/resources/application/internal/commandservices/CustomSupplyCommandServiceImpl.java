package com.uitopic.restock.platform.resources.application.internal.commandservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyImageCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyPerishableCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyContent;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyCommandService;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class CustomSupplyCommandServiceImpl implements CustomSupplyCommandService {

    private final CustomSupplyRepository repository;
    private final BatchRepository batchRepository;
    private final SupplyRepository supplyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageService imageService;

    private static final String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/deuy1pr9e/image/upload/v1780190808/restock_default_supply_image.jpg";
    private static final String DEFAULT_PUBLIC_ID = "restock_default_supply_image";

    public CustomSupplyCommandServiceImpl(CustomSupplyRepository repository,
                                          BatchRepository batchRepository,
                                          SupplyRepository supplyRepository,
                                          ApplicationEventPublisher eventPublisher,
                                          ImageService imageService) {
        this.repository = repository;
        this.batchRepository = batchRepository;
        this.supplyRepository = supplyRepository;
        this.eventPublisher = eventPublisher;
        this.imageService = imageService;
    }

    @Override
    public CustomSupply handle(CreateCustomSupplyCommand command) {
        log.info("Creating custom supply '{}' for account ID: {}", command.name(), command.accountId());

        AccountId accountId = new AccountId(command.accountId());
        if (repository.existsByAccountIdAndName(accountId, command.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Supply with name '" + command.name() + "' already exists for this account");
        }

        Supply category = supplyRepository.findById(command.supplyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Supply template not found: " + command.supplyId()));

        Money unitPrice = new Money(BigDecimal.valueOf(command.unitPrice()), command.unitPriceCurrencyCode());

        String imageUrl = null;
        String publicId = null;

        if (command.hasImage()) {
            try {
                var uploadResult = imageService.upload(command.image(), command.imageFileName());
                imageUrl = uploadResult.get("url");
                publicId = uploadResult.get("publicId");
                log.info("Image uploaded to Cloudinary — publicId: {}", publicId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error uploading image to storage: " + e.getMessage());
            }
        } else {
            imageUrl = DEFAULT_IMAGE_URL;
            publicId = DEFAULT_PUBLIC_ID;
        }

        CustomSupply cs = CustomSupply.builder()
                .accountId(accountId)
                .name(command.name())
                .description(command.description())
                .supply(category)
                .unitPrice(unitPrice)
                .content(new SupplyContent(command.supplyContent()))
                .unitMeasurement(new UnitMeasurement(command.unitMeasurement()))
                .imageUrl(new ImageURL(imageUrl, publicId))
                .build();

        CustomSupply saved = repository.save(cs);
        log.info("Custom supply created — ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Optional<CustomSupply> update(String id, CreateCustomSupplyCommand command) {
        return repository.findById(id).map(existing -> {
            log.info("Updating custom supply ID: {}", id);

            Supply category = supplyRepository.findById(command.supplyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Supply template not found: " + command.supplyId()));

            Money unitPrice = new Money(BigDecimal.valueOf(command.unitPrice()), command.unitPriceCurrencyCode());

            String oldPublicId = existing.getImageUrl() != null && !DEFAULT_PUBLIC_ID.equals(existing.getImageUrl().publicId())
                    ? existing.getImageUrl().publicId()
                    : null;

            String newImageUrl = null;
            String newPublicId = null;

            if (command.hasImage()) {
                try {
                    var uploadResult = imageService.upload(command.image(), command.imageFileName());
                    newImageUrl = uploadResult.get("url");
                    newPublicId = uploadResult.get("publicId");
                    log.info("New image uploaded to Cloudinary — publicId: {}", newPublicId);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error uploading image to storage: " + e.getMessage());
                }
            } else {
                // Keep existing image if none provided
                newImageUrl = existing.getImageUrl().getUrl();
                newPublicId = existing.getImageUrl().publicId();
            }

            existing.update(null, unitPrice, command.description(),
                    new SupplyContent(command.supplyContent()),
                    new UnitMeasurement(command.unitMeasurement()));

            existing.setSupply(category);
            existing.setName(command.name());
            existing.setImageUrl(new ImageURL(newImageUrl, newPublicId));

            CustomSupply saved = repository.save(existing);

            if (oldPublicId != null && !oldPublicId.equals(newPublicId)) {
                try {
                    imageService.delete(oldPublicId);
                    log.info("Old image deleted from Cloudinary — publicId: {}", oldPublicId);
                } catch (Exception e) {
                    log.warn("Could not delete old image [{}]: {}", oldPublicId, e.getMessage());
                }
            }

            log.info("Custom supply updated — ID: {}", id);
            return saved;
        });
    }

    @Override
    public Optional<CustomSupply> updatePerishable(UpdateCustomSupplyPerishableCommand command) {
        return repository.findById(command.id()).map(existing -> {
            log.info("PATCH perishable status — ID: {}, isPerishable: {}", command.id(), command.isPerishable());
            existing.updatePerishable(command.isPerishable());
            CustomSupply saved = repository.save(existing);
            log.info("Perishable status updated — ID: {}", command.id());
            return saved;
        });
    }

    @Override
    public Optional<CustomSupply> updateImage(UpdateCustomSupplyImageCommand command) {
        return repository.findById(command.id()).map(existing -> {
            log.info("PATCH image — ID: {}", command.id());

            String newImageUrl;
            String newPublicId;
            try {
                var uploadResult = imageService.upload(command.image(), command.imageFileName());
                newImageUrl = uploadResult.get("url");
                newPublicId = uploadResult.get("publicId");
                log.info("New image uploaded — publicId: {}", newPublicId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Error uploading image: " + e.getMessage());
            }

            String oldPublicId = existing.getImageUrl() != null
                    && !DEFAULT_PUBLIC_ID.equals(existing.getImageUrl().publicId())
                    ? existing.getImageUrl().publicId() : null;

            existing.setImageUrl(new ImageURL(newImageUrl, newPublicId));
            CustomSupply saved = repository.save(existing);

            if (oldPublicId != null) {
                try {
                    imageService.delete(oldPublicId);
                    log.info("Old image deleted — publicId: {}", oldPublicId);
                } catch (Exception e) {
                    log.warn("Could not delete old image [{}]: {}", oldPublicId, e.getMessage());
                }
            }

            return saved;
        });
    }

    @Override
    public void delete(String id) {
        repository.findById(id).ifPresentOrElse(cs -> {
            boolean hasStock = batchRepository.findByCustomSupplyId(id)
                    .stream().anyMatch(b -> b.getCurrentStock().getValue() > 0);
            if (hasStock) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Supply has active batches with stock — deplete them first");
            }

            if (cs.getImageUrl() != null && !DEFAULT_PUBLIC_ID.equals(cs.getImageUrl().publicId())) {
                try {
                    imageService.delete(cs.getImageUrl().publicId());
                    log.info("Image deleted from Cloudinary — publicId: {}", cs.getImageUrl().publicId());
                } catch (Exception e) {
                    log.warn("Could not delete image [{}]: {}", cs.getImageUrl().publicId(), e.getMessage());
                }
            }

            repository.deleteById(id);
            eventPublisher.publishEvent(new CustomSupplyDeletedEvent(id, cs.getAccountId().getAccountId()));
            log.info("Custom supply deleted — ID: {}", id);
        }, () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom supply not found: " + id); });
    }
}