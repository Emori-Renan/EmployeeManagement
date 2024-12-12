package com.example.crudapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.crudapp.dto.AuthenticationResponse;
import com.example.crudapp.model.UserLogin;
import com.example.crudapp.repository.UserLoginRepository;
import com.example.crudapp.util.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserLoginRepository userLoginRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserLoginRepository userLoginRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userLoginRepository = userLoginRepository;
    }

    public ResponseEntity<AuthenticationResponse> authenticate(String usernameOrEmail, String rawPassword) {
        String username;
        UserLogin user = null;

        if (isEmail(usernameOrEmail)) {
            user = userLoginRepository.findByEmail(usernameOrEmail).orElse(null);
            if (user == null) {
                AuthenticationResponse errorResponse = new AuthenticationResponse(null,
                        "User not found with email: " + usernameOrEmail);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            username = user.getUsername();
        } else {
            user = userLoginRepository.findByUsername(usernameOrEmail).orElse(null);
            if (user == null) {
                AuthenticationResponse errorResponse = new AuthenticationResponse(null,
                        "User not found with username: " + usernameOrEmail);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            username = user.getUsername();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword));
            UserLogin authenticatedUser = (UserLogin) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authenticatedUser.getUsername(), authenticatedUser.getRole());
            AuthenticationResponse successResponse = new AuthenticationResponse(token, "Login successful");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            AuthenticationResponse errorResponse = new AuthenticationResponse(null, "Invalid password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    private boolean isEmail(String usernameOrEmail) {
        return usernameOrEmail.contains("@");
    }

}
