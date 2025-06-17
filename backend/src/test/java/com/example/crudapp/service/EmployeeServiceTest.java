package com.example.crudapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.crudapp.dto.EmployeeDTO;
import com.example.crudapp.dto.EmployeeRegistrationDTO;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.UserLogin;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.UserLoginRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRegistrationDTO validDTO;
    private UserLogin userLogin;
    private Employee savedEmployee;

    @BeforeEach
    void setUp() {
        // Prepare test data
        validDTO = new EmployeeRegistrationDTO();
        validDTO.setEmployeeName("John Doe");
        validDTO.setRole("employee");
        validDTO.setUsername("abc");

        userLogin = new UserLogin();
        userLogin.setId(1L);
        userLogin.setUsername("abc");

        savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setEmployeeName("John Doe");
        savedEmployee.setRole("employee");
        savedEmployee.setUserLogin(userLogin);

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
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(userLoginRepository.findByUsername("abc")).thenReturn(Optional.of(userLogin));
        // Act
        ServiceResponse response = employeeService.registerEmployee(validDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Employee registered successfully", response.getMessage());
        assertNotNull(response.getData());

        EmployeeDTO returnedEmployee = (EmployeeDTO) response.getData();
        assertEquals("John Doe", returnedEmployee.getEmployeeName());
        assertEquals("employee", returnedEmployee.getRole());
        assertEquals(1L, returnedEmployee.getId());

        // Verify repository interactions
        verify(userLoginRepository, times(1)).findByUsername("abc");
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verifyNoMoreInteractions(userLoginRepository, employeeRepository);
    }

}