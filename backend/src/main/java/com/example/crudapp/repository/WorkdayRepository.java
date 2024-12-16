package com.example.crudapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.Workday;

public interface WorkdayRepository extends JpaRepository<Workday, Long> {
    Optional<Workday> findByEmployeeIdAndWorkplaceIdAndDate(Long employeeId, Long workplaceId, LocalDate date);
    List<Workday> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
