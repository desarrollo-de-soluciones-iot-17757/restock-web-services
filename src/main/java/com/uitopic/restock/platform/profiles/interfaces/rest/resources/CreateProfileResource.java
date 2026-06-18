package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "CreateProfileResource", description = "Request resource for creating a user profile")
public record CreateProfileResource(
        @NotBlank String userId,
        @NotBlank String name,
        String lastName,
        String phoneNumber,
        String gender,
        String birthDate,
        MultipartFile image
) {}
