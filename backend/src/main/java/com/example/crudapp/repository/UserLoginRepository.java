package com.example.crudapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
 // Método para encontrar um usuário pelo nome de usuário
 Optional<UserLogin> findByUsername(String username);

 // Método para encontrar um usuário pelo email
 Optional<UserLogin> findByEmail(String email);

 // Método para encontrar um usuário pelo nome de usuário ou email
 Optional<UserLogin> findByUsernameOrEmail(String username, String email);
} 