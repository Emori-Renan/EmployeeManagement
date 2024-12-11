package com.example.crudapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.crudapp.dto.UserRegistrationResult;
import com.example.crudapp.model.UserLogin;
import com.example.crudapp.repository.UserLoginRepository;

@Service
public class UserLoginService {

    @Autowired
    private UserLoginRepository userRepository;

    public UserRegistrationResult registerUser(String username, String email, String rawPassword, String role) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(rawPassword);

        UserLogin user = new UserLogin();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(role);

        try {
            userRepository.save(user);
            return new UserRegistrationResult(true, "User registered successfully");
        } catch (Exception e) {
            return new UserRegistrationResult(false, "Unexpected error ocurred." + e);
        }
    }
}
