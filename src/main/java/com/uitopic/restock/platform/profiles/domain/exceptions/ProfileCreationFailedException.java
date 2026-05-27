package com.uitopic.restock.platform.profiles.domain.exceptions;

public class ProfileCreationFailedException extends RuntimeException {
    public ProfileCreationFailedException(String message) {
        super(message);
    }
}
