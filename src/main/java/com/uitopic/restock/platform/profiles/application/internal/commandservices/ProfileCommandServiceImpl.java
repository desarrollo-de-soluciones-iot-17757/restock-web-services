package com.uitopic.restock.platform.profiles.application.internal.commandservices;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.DeleteProfileCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.UpdateProfileCommand;
import com.uitopic.restock.platform.profiles.domain.repositories.ProfileRepository;
import com.uitopic.restock.platform.profiles.domain.services.ProfileCommandService;
import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;
import com.uitopic.restock.platform.shared.domain.exceptions.ImageUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final ImageService imageService;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository, ImageService imageService) {
        this.profileRepository = profileRepository;
        this.imageService = imageService;
    }

    @Override
    public Profile handle(CreateProfileCommand command) {
        log.info("Creating profile for userId='{}'", command.userId());

        String avatarUrl = null;
        String avatarPublicId = null;
        if (command.hasImage()) {
            try {
                var result = imageService.upload(command.image(), command.photoFileName());
                avatarUrl = result.get("url");
                avatarPublicId = result.get("publicId");
            } catch (Exception e) {
                throw new ImageUploadException("Error uploading profile avatar: " + e.getMessage());
            }
        }

        var profile = new Profile(command.accountId(), command.userId(), command.name(), command.lastName(),
                command.phoneNumber(), avatarUrl, avatarPublicId, command.gender(), command.birthDate());
        var saved = profileRepository.save(profile);
        log.info("Profile created: id='{}'", saved.getId());
        return saved;
    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command) {
        log.info("Updating profile id='{}'", command.id());
        return profileRepository.findById(command.id()).map(profile -> {
            String previousPublicId = profile.hasDefaultAvatar()
                    ? null
                    : profile.getAvatarPublicId();
            String avatarUrl = profile.getAvatarUrl();
            String avatarPublicId = profile.getAvatarPublicId();

            if (command.hasImage()) {
                try {
                    var result = imageService.upload(command.image(), command.photoFileName());
                    avatarUrl = result.get("url");
                    avatarPublicId = result.get("publicId");
                } catch (Exception e) {
                    throw new ImageUploadException("Error uploading profile avatar: " + e.getMessage());
                }
            }

            profile.update(command.name(), command.lastName(), command.phoneNumber(),
                    avatarUrl, avatarPublicId, command.gender(), command.birthDate());
            var updated = profileRepository.save(profile);

            if (command.hasImage() && previousPublicId != null) {
                try {
                    imageService.delete(previousPublicId);
                } catch (Exception e) {
                    log.warn("Could not delete previous avatar: publicId='{}', error='{}'",
                            previousPublicId, e.getMessage());
                }
            }

            return updated;
        });
    }

    @Override
    public void handle(DeleteProfileCommand command) {
        log.info("Deleting profile id='{}'", command.id());
        profileRepository.findById(command.id()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found: " + command.id()));
        profileRepository.deleteById(command.id());
    }
}
