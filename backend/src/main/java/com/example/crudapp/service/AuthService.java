package com.example.crudapp.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.crudapp.model.UserLogin;
import com.example.crudapp.repository.UserLoginRepository;
import com.example.crudapp.util.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserLoginRepository userLoginRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserLoginRepository userLoginRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userLoginRepository = userLoginRepository;
    }

    public String authenticate(String usernameOrEmail, String rawPassword) {
        String username = usernameOrEmail;
        if (isEmail(usernameOrEmail)) {
            UserLogin user = userLoginRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + usernameOrEmail));
            username = user.getUsername(); 
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword)
        );

        UserLogin user = (UserLogin) authentication.getPrincipal();
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

    private boolean isEmail(String usernameOrEmail) {
        return usernameOrEmail.contains("@");
    }

}
