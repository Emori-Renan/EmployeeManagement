package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudapp.model.User;
import com.example.crudapp.repository.UserRepository;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

     @Test
     void testCreateUser() {
        User newUser = new User(null, "Jane", "jane@example.com", 28);
        User savedUser = userService.saveUser(newUser);

        assertNotNull(savedUser.getId());  // Ensure that the ID is generated.
        assertEquals("Jane", savedUser.getName());
    }

    @Test
     void testGetUserById() {
        User newUser = new User(null, "Tom", "tom@example.com", 35);
        User savedUser = userService.saveUser(newUser);

        User foundUser = userService.getUserById(savedUser.getId()).orElseThrow();

        assertEquals("Tom", foundUser.getName());
    }

    @Test
     void testDeleteUser() {
        User newUser = new User(null, "Alice", "alice@example.com", 25);
        User savedUser = userService.saveUser(newUser);

        userService.deleteUser(savedUser.getId());

        assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }
}
