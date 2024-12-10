package com.example.crudapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.crudapp.dto.UserLoginResponseDTO;
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

    public ResponseEntity<?> authenticate(String usernameOrEmail, String rawPassword) {
        String username = usernameOrEmail;
        UserLogin user = null;

        if (isEmail(usernameOrEmail)) {
            user = userLoginRepository.findByEmail(usernameOrEmail).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with email: " + usernameOrEmail);
            }
            username = user.getUsername();
        } else {
            user = userLoginRepository.findByUsername(usernameOrEmail).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with username: " + usernameOrEmail);
            }
            username = user.getUsername();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword)
            );
            UserLogin authenticatedUser = (UserLogin) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authenticatedUser.getUsername(), authenticatedUser.getRole());
            return ResponseEntity.ok(new UserLoginResponseDTO(token, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password.");
        }
    }

    private boolean isEmail(String usernameOrEmail) {
        return usernameOrEmail.contains("@");
    }

}
