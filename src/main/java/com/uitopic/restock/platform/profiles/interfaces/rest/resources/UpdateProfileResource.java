package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "UpdateProfileResource", description = "Request resource for partially updating a user profile")
public record UpdateProfileResource(
        String name,
        String lastName,
        String phoneNumber,
        String gender,
        String birthDate,
        MultipartFile image
) {}
