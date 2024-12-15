package com.example.crudapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.crudapp.dto.EmployeeRegistrationDTO;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.model.Employee;
import com.example.crudapp.repository.EmployeeRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerEmployee_shouldReturnError_whenNameOrRoleIsEmpty() {
        // Arrange
        EmployeeRegistrationDTO emptyNameDTO = new EmployeeRegistrationDTO();
        emptyNameDTO.setEmployeeName("");
        emptyNameDTO.setRole("admin");

        EmployeeRegistrationDTO emptyRoleDTO = new EmployeeRegistrationDTO();
        emptyRoleDTO.setEmployeeName("John");
        emptyRoleDTO.setRole("");

        // Act
        ServiceResponse response1 = employeeService.registerEmployee(emptyNameDTO);
        ServiceResponse response2 = employeeService.registerEmployee(emptyRoleDTO);

        // Assert
        assertFalse(response1.isSuccess());
        assertEquals("A name and a role are necessary.", response1.getMessage());

        assertFalse(response2.isSuccess());
        assertEquals("A name and a role are necessary.", response2.getMessage());
    }

    @Test
    void registerEmployee_shouldReturnError_whenRoleIsInvalid() {
        // Arrange
        EmployeeRegistrationDTO invalidRoleDTO = new EmployeeRegistrationDTO();
        invalidRoleDTO.setEmployeeName("John");
        invalidRoleDTO.setRole("manager");

        // Act
        ServiceResponse response = employeeService.registerEmployee(invalidRoleDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Invalid role. Role must be 'admin' or 'employee'.", response.getMessage());
    }

    @Test
    void registerEmployee_shouldRegisterEmployeeSuccessfully() {
        // Arrange
        EmployeeRegistrationDTO validDTO = new EmployeeRegistrationDTO();
        validDTO.setEmployeeName("John");
        validDTO.setRole("employee");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setEmployeeName("John");
        savedEmployee.setRole("employee");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // Act
        ServiceResponse response = employeeService.registerEmployee(validDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Employee registered successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("John", ((Employee) response.getData()).getEmployeeName());
        assertEquals("EMPLOYEE", ((Employee) response.getData()).getRole());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void registerEmployee_shouldHandleExceptions() {
        // Arrange
        EmployeeRegistrationDTO validDTO = new EmployeeRegistrationDTO();
        validDTO.setEmployeeName("John");
        validDTO.setRole("employee");

        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        ServiceResponse response = employeeService.registerEmployee(validDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("An error occurred while registering the employee"));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
}
