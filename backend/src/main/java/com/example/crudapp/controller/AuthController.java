package com.example.crudapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO request) {
        String token = authService.authenticate(request.getUsernameOrEmail(), request.getPassword());
        return new UserLoginResponseDTO(token, "Login successful");
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
