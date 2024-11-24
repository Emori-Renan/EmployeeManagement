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

    // Create or Update (upsert) User
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);  // This will save or update the user
    }

    // Get a User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);  // Returns an Optional<User>
    }

    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Returns a list of users
    }

    // Update a User (via save)
    @Transactional
    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);  // Set the ID to ensure the right user is updated
            return userRepository.save(user);  // This will update the user
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // Delete User by ID
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);  // Deletes the user
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
