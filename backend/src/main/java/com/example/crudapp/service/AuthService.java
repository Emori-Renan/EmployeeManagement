package com.example.crudapp.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.crudapp.model.UserLogin;
import com.example.crudapp.util.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String usernameOrEmail, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(usernameOrEmail, rawPassword)
        );

        UserLogin user = (UserLogin) authentication.getPrincipal();
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

}
