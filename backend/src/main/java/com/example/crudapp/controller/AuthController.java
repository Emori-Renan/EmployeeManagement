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

import com.example.crudapp.dto.UserLoginRequestDTO;
import com.example.crudapp.dto.UserLoginResponseDTO;
import com.example.crudapp.dto.UserRegistrationDTO;
import com.example.crudapp.dto.UserRegistrationResult;
import com.example.crudapp.service.AuthService;
import com.example.crudapp.service.UserLoginService;

import jakarta.validation.Valid;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO request) {
        return authService.authenticate(request.getUsernameOrEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResult> register(
            @RequestBody @Valid UserRegistrationDTO request, BindingResult result) {

        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new UserRegistrationResult(false, sb.toString()));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new UserRegistrationResult(true, "User registered successfully"));
    }
    
}
