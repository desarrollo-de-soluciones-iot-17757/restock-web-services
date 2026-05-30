package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public class UsernamePasswordAuthenticationTokenBuilder {

    public static UsernamePasswordAuthenticationToken build(UserDetails userDetails, HttpServletRequest request) {
        var token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        token.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
                .buildDetails(request));
        return token;
    }
}
