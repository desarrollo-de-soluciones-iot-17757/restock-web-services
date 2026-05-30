package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.model;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of Spring Security's {@link UserDetails}
 * interface for the {@link User} aggregate.
 *
 * This class adapts the {@link User} domain entity to the security
 * framework, providing necessary authentication details such as username,
 * password, and authorities.
 */
public class UserDetailsImpl implements UserDetails {

    private final User user;

    /**
     * Constructs a {@code UserDetailsImpl} with the specified {@link User}.
     * 
     * @param user the user aggregate to adapt for security context
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * Gets the username (email address) of the user.
     * 
     * @return the user's email address as a String
     */
    @Override
    public String getUsername() {
        return user.getEmail().email();
    }

    /**
     * Gets the encoded password of the user.
     * 
     * @return the user's password hash as a String
     */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    /**
     * Gets the authorities (roles) granted to the user.
     * 
     * @return a collection of {@link GrantedAuthority} representing the user's
     *         roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getType().name()));
    }

    /**
     * Checks if the account is not expired.
     * 
     * @return {@code true} indicating the account is never expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the account is not locked.
     * 
     * @return {@code true} indicating the account is never locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the credentials (password) are not expired.
     * 
     * @return {@code true} indicating the credentials are never expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the account is enabled.
     * 
     * @return {@code true} indicating the account is always enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
