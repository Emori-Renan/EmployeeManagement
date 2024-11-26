package com.example.crudapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.crudapp.model.User;
import com.example.crudapp.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);  
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id); 
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); 
    }

    @Transactional
    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id); 
            return userRepository.save(user); 
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id); 
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
