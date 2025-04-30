package com.ERP.erp.user.service;

import com.ERP.erp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    /**
     * Locates the user based on the username. 
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (an object implementing UserDetails)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Use the UserRepository to find the user by their username in the database

        // Return the User entity. Since your User class now implements UserDetails,
        // Spring Security can use this object directly.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
