package com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {

    String generateToken(User user);

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
