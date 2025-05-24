package com.example.crudapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crudapp.model.Workday;

public interface WorkdayRepository extends JpaRepository<Workday, Long> {
    Optional<Workday> findByEmployeeIdAndWorkplaceIdAndDate(Long employeeId, Long workplaceId, LocalDate date);
    List<Workday> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
    List<Workday> findByEmployeeIdAndWorkplaceId(Long employeeId, Long workplaceId);
    List<Workday> findByEmployeeId(Long employeeId);
    List<Workday> findByEmployeeIdAndDateBetweenAndWorkplaceId(Long id, LocalDate start, LocalDate end, Long workplaceId);
}
