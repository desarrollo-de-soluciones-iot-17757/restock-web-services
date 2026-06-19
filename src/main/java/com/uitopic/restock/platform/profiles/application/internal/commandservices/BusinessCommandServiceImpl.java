package com.uitopic.restock.platform.profiles.application.internal.commandservices;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.domain.model.commands.CreateBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.DeleteBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.UpdateBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.repositories.BusinessRepository;
import com.uitopic.restock.platform.profiles.domain.services.BusinessCommandService;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.exceptions.ImageUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class BusinessCommandServiceImpl implements BusinessCommandService {

    private final BusinessRepository businessRepository;
    private final ImageService imageService;

    public BusinessCommandServiceImpl(BusinessRepository businessRepository, ImageService imageService) {
        this.businessRepository = businessRepository;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public Business handle(CreateBusinessCommand command) {
        log.info("Creating business for userId='{}'", command.userId());

        String pictureUrl = null;
        String picturePublicId = null;
        if (command.hasImage()) {
            try {
                var result = imageService.upload(command.image(), command.photoFileName());
                pictureUrl = result.get("url");
                picturePublicId = result.get("publicId");
            } catch (Exception e) {
                throw new ImageUploadException("Error uploading business picture: " + e.getMessage());
            }
        }

        var business = new Business(command.userId(), command.ruc(), pictureUrl, picturePublicId,
                command.companyName(), command.mainLocation());
        var saved = businessRepository.save(business);
        log.info("Business created: id='{}'", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Optional<Business> handle(UpdateBusinessCommand command) {
        log.info("Updating business id='{}'", command.id());
        return businessRepository.findById(command.id()).map(business -> {
            String previousPublicId = business.getPicturePublicId();
            String pictureUrl = business.getPictureUrl();
            String picturePublicId = business.getPicturePublicId();

            if (command.hasImage()) {
                try {
                    var result = imageService.upload(command.image(), command.photoFileName());
                    pictureUrl = result.get("url");
                    picturePublicId = result.get("publicId");
                } catch (Exception e) {
                    throw new ImageUploadException("Error uploading business picture: " + e.getMessage());
                }
            }

            business.update(command.ruc(), pictureUrl, picturePublicId,
                    command.companyName(), command.mainLocation());
            var updated = businessRepository.save(business);

            if (command.hasImage() && previousPublicId != null) {
                try {
                    imageService.delete(previousPublicId);
                } catch (Exception e) {
                    log.warn("Could not delete previous picture: publicId='{}', error='{}'",
                            previousPublicId, e.getMessage());
                }
            }

            return updated;
        });
    }

    @Override
    @Transactional
    public void handle(DeleteBusinessCommand command) {
        log.info("Deleting business id='{}'", command.id());
        businessRepository.findById(command.id()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found: " + command.id()));
        businessRepository.deleteById(command.id());
    }
}
