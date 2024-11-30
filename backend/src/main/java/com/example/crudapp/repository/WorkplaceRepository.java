package com.example.crudapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.Workplace;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    Optional<Workplace> findByName(String name);
}
