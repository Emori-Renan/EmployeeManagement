package com.example.crudapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
 Optional<UserLogin> findByUsername(String username);

 Optional<UserLogin> findByEmail(String email);

 Optional<UserLogin> findByUsernameOrEmail(String username, String email);
} 