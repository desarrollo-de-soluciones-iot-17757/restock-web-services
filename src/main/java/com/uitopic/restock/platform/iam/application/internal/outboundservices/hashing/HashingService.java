package com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing;

/**
 * Service interface for password hashing and verification operations.
 * Defines contract for encoding raw passwords and matching them against encoded
 * hashes.
 */
public interface HashingService {

    /**
     * Encodes a raw password into a hashed string representation.
     * 
     * @param rawPassword the plain text password to encode
     * @return the encoded (hashed) password string
     */
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
