package com.example.crudapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.crudapp.model.Workplace;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    
    Optional<Workplace> findByWorkplaceName(String name);

    List<Workplace> findAllByEmployeeId(Long employeeId);
}
