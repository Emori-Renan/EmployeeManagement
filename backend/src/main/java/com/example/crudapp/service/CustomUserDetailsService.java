package com.example.crudapp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.crudapp.repository.UserLoginRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserLoginRepository userLoginRepository;

    public CustomUserDetailsService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Primeiro, tenta buscar pelo email
        return userLoginRepository.findByEmail(usernameOrEmail)
                .orElseGet(() -> userLoginRepository.findByUsername(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)));

    }

}
