package com.uitopic.restock.platform.iam.infrastructure.hashing.bcrypt;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;

/**
 * Marker interface for BCrypt hashing service implementations.
 * <p>
 * This interface extends {@link HashingService} to identify implementations
 * that
 * use BCrypt for password hashing and verification.
 * It serves as a type marker for dependency injection and configuration.
 */
public interface BCryptHashingService extends HashingService {
}
