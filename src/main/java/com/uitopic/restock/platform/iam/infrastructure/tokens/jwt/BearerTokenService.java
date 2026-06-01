package com.uitopic.restock.platform.iam.infrastructure.tokens.jwt;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens.TokenService;

/**
 * Marker interface for Bearer token service implementations.
 * <p>
 * This interface extends {@link TokenService} to identify implementations that
 * handle
 * Bearer token generation, validation, and extraction. It serves as a type
 * marker for
 * dependency injection and configuration purposes.
 */
public interface BearerTokenService extends TokenService {
}
