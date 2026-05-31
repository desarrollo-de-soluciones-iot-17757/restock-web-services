package com.uitopic.restock.platform.iam.infrastructure.hashing.bcrypt.services;

import com.uitopic.restock.platform.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BCryptHashingService} that uses Spring Security's
 * {@link BCryptPasswordEncoder} to hash passwords and verify them.
 */
@Service
public class HashingServiceImpl implements BCryptHashingService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Encodes a raw character sequence (password) using the BCrypt hashing function.
     *
     * @param rawPassword the raw password to be encoded
     * @return the bcrypt-encoded password hash
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Checks whether the given raw password matches the stored BCrypt password hash.
     *
     * @param rawPassword the plain-text password to check
     * @param encodedPassword the BCrypt hash stored in the database
     * @return true if the passwords match, false otherwise
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
