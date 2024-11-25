package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudapp.model.User;
import com.example.crudapp.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional 
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Criação de um usuário antes de cada teste
        testUser = new User(null, "John Doe", "johndoe@example.com", 30);
    }

     @Test
     void testCreateUser() {
        User savedUser = userService.saveUser(testUser);
        assertNotNull(savedUser.getId());  // Ensure that the ID is generated.
        assertEquals("John Doe", savedUser.getName());
    }

    @Test
     void testGetUserById() {
        User savedUser = userService.saveUser(testUser);

        User foundUser = userService.getUserById(savedUser.getId()).orElseThrow();

        assertEquals("John Doe", foundUser.getName());
    }

    @Test
     void testDeleteUser() {
        User savedUser = userService.saveUser(testUser);

        userService.deleteUser(savedUser.getId());

        assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }
}
