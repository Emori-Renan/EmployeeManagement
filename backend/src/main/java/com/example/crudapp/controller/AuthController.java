package com.example.crudapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.dto.AuthenticationResponse;
import com.example.crudapp.dto.UserLoginRequestDTO;
import com.example.crudapp.dto.UserRegistrationDTO;
import com.example.crudapp.dto.UserRegistrationResult;
import com.example.crudapp.service.AuthService;
import com.example.crudapp.service.UserLoginService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

    private final UserLoginService userLoginService;
    private final AuthService authService;

    public AuthController(UserLoginService userLoginService, AuthService authService) {
        this.userLoginService = userLoginService;
        this.authService = authService;
    }

    @Operation(summary = "Login User", description = "Authenticates a user and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserLoginRequestDTO request) {
        return authService.authenticate(request.getUsernameOrEmail(), request.getPassword());
    }

    @Operation(summary = "Register User", description = "Registers a new user with a username, email, password, and role.")
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResult> register(
            @RequestBody @Valid UserRegistrationDTO request, BindingResult result) {

        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserRegistrationResult(false, sb.toString()));
        }
        UserRegistrationResult registrationResult = userLoginService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole());
        if (registrationResult.isSuccess()) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(registrationResult);
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(registrationResult);
    }
    }

}
