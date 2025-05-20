package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.crudapp.dto.SalaryDto;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Salary;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.SalaryRepository;

public class SalaryServiceTest {

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SalaryService salaryService;

    private Employee employee;
    private SalaryDto salaryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeName("John Doe");

        salaryDto = new SalaryDto();
        salaryDto.setEmployeeId(1L);
        salaryDto.setMonthYear("2025-05");
        salaryDto.setTotalDaysWorked(20);
        salaryDto.setTotalHours(160.0);
        salaryDto.setOvertimeHours(5.0);
        salaryDto.setTotalTransportCost(50.0);
        salaryDto.setFinalSalary(3000.0);
    }

    @Test
    void registerSalary_success() {
        // Arrange
        when(salaryRepository.findByEmployeeIdAndMonthYear(1L, "2025-05"))
                .thenReturn(Optional.empty());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(salaryRepository.save(any(Salary.class))).thenReturn(createSalaryEntity());

        // Act
        ServiceResponse response = salaryService.registerSalary(salaryDto);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Salary registered successfully", response.getMessage());
        assertNotNull(response.getData());
        assertTrue(response.getData() instanceof Salary);

        Salary savedSalary = (Salary) response.getData();
        assertEquals(employee, savedSalary.getEmployee());
        assertEquals("2025-05", savedSalary.getMonthYear());
        assertEquals(20, savedSalary.getTotalDaysWorked());
        assertEquals(160.0, savedSalary.getTotalHours());
        assertEquals(5.0, savedSalary.getOvertimeHours());
        assertEquals(50.0, savedSalary.getTotalTransportCost());
        assertEquals(3000.0, savedSalary.getFinalSalary());
        verify(salaryRepository, times(1)).findByEmployeeIdAndMonthYear(1L, "2025-05");
        verify(employeeRepository, times(1)).findById(1L);
        verify(salaryRepository, times(1)).save(any(Salary.class));
    }

    @Test
    void registerSalary_alreadyExists() {
        // Arrange
        when(salaryRepository.findByEmployeeIdAndMonthYear(1L, "2025-05"))
                .thenReturn(Optional.of(createSalaryEntity()));
        when(salaryRepository.findByEmployeeIdAndMonthYear(1L, "2025-05"))
                .thenReturn(Optional.of(createSalaryEntity()));
        ServiceResponse response = salaryService.registerSalary(salaryDto);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Salary already exists for this employee for this month/year.", response.getMessage());
        assertNull(response.getData());

        verify(salaryRepository, times(1)).findByEmployeeIdAndMonthYear(1L, "2025-05");
        verify(employeeRepository, never()).findById(anyLong());
        verify(salaryRepository, never()).save(any(Salary.class));
    }

    @Test
    void registerSalary_employeeNotFound() {
        // Arrange
        when(salaryRepository.findByEmployeeIdAndMonthYear(1L, "2025-05"))
                .thenReturn(Optional.empty());
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ServiceResponse response = salaryService.registerSalary(salaryDto);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Employee not found with ID: 1", response.getMessage());
        assertNull(response.getData());

        verify(salaryRepository, times(1)).findByEmployeeIdAndMonthYear(1L, "2025-05");
        verify(employeeRepository, times(1)).findById(1L);
        verify(salaryRepository, never()).save(any(Salary.class));
    }

    private Salary createSalaryEntity() {
        Salary salary = new Salary();
        salary.setId(1L);
        salary.setEmployee(employee);
        salary.setMonthYear("2025-05");
        salary.setTotalDaysWorked(20);
        salary.setTotalHours(160.0);
        salary.setOvertimeHours(5.0);
        salary.setTotalTransportCost(50.0);
        salary.setFinalSalary(3000.0);
        return salary;
    }
}