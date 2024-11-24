package com.example.crudapp.repository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<String> createUser(String name, String email, int age){
        String sql = "INSERT INTO users VALUES (?, ?, ?)";
        try{
            int rowsAffected = jdbcTemplate.update(sql, name, email, age);
            if(rowsAffected > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to create user");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<String> udpateUser(String name, String email, int age){
        String sql = "UPDATE users SET name = ?, email = ?, age = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, name, email, age);
            if(rowsAffected > 0){
                return ResponseEntity.ok("User updated successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    } 

}
