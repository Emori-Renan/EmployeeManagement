package com.example.crudapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.Workday;

public interface WorkdayRepository extends JpaRepository<Workday, Long> {
    List<Workday> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
