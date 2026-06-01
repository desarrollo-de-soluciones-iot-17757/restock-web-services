package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.services;

import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of Spring Security's {@link UserDetailsService} interface.
 * Loads user-specific data from the database using their email as username.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email address.
     * Searches the MongoDB database for the user and constructs a UserDetails wrapper.
     *
     * @param email the email address identifying the user whose data is required
     * @return the {@link UserDetails} containing the user's login and authorization details
     * @throws UsernameNotFoundException if the user with the specified email cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(email))
                 .map(UserDetailsImpl::new)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
