package com.example.crudapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.dto.UserLoginRequestDTO;
import com.example.crudapp.dto.UserLoginResponseDTO;
import com.example.crudapp.dto.UserRegistrationDTO;
import com.example.crudapp.service.AuthService;
import com.example.crudapp.service.UserLoginService;


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
    public String register(@RequestBody UserRegistrationDTO request) {
        userLoginService.registerUser(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getRole()
        );
        return "User registered successfully";
    }
}
