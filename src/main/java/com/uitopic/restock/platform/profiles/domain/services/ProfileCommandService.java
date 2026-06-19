package com.uitopic.restock.platform.profiles.domain.services;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.DeleteProfileCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.UpdateProfileCommand;

import java.util.Optional;

public interface ProfileCommandService {
    Profile handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
    void handle(DeleteProfileCommand command);
}
