package com.uitopic.restock.platform.profiles.interfaces.rest.controllers;

import com.uitopic.restock.platform.profiles.domain.model.commands.DeleteProfileCommand;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetAllProfilesQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByAccountIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByUserIdQuery;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.profiles.domain.services.ProfileCommandService;
import com.uitopic.restock.platform.profiles.domain.services.ProfileQueryService;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.CreateProfileResource;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.ProfileResource;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.uitopic.restock.platform.profiles.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.uitopic.restock.platform.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.uitopic.restock.platform.profiles.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "User profile management endpoints")
public class UserProfilesController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public UserProfilesController(ProfileCommandService profileCommandService,
                                  ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @Operation(summary = "Get profiles with optional filters")
    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAll(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String userId
    ) {
        var profiles = accountId != null && !accountId.isBlank()
                ? profileQueryService.handle(new GetProfileByAccountIdQuery(new AccountId(accountId)))
                : userId != null && !userId.isBlank()
                        ? profileQueryService.handle(new GetProfileByUserIdQuery(userId))
                        : profileQueryService.handle(new GetAllProfilesQuery());

        return ResponseEntity.ok(profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @Operation(summary = "Create profile")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileResource> create(@Valid @ModelAttribute CreateProfileResource resource) {
        var command = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = profileCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProfileResourceFromEntityAssembler.toResourceFromEntity(profile));
    }

    @Operation(summary = "Get profile by ID")
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResource> getById(@PathVariable String profileId) {
        var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Profile not found: " + profileId));
        return ResponseEntity.ok(ProfileResourceFromEntityAssembler.toResourceFromEntity(profile));
    }

    @Operation(summary = "Update profile")
    @PatchMapping(value = "/{profileId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileResource> update(
            @PathVariable String profileId,
            @Valid @ModelAttribute UpdateProfileResource resource
    ) {
        var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(profileId, resource);
        var profile = profileCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Profile not found: " + profileId));
        return ResponseEntity.ok(ProfileResourceFromEntityAssembler.toResourceFromEntity(profile));
    }

    @Operation(summary = "Delete profile")
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String profileId) {
        profileCommandService.handle(new DeleteProfileCommand(profileId));
        return ResponseEntity.ok(Map.of(
                "id", profileId,
                "deletedAt", Instant.now().toString()
        ));
    }
}
