package com.example.crudapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long>{
    Optional<Salary> findByEmployeeIdAndMonthYear(Long employeeId, String monthYear);
}
