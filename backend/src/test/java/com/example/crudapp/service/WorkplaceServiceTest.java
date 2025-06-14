package com.example.crudapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkplaceRegistrationDTO;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.WorkplaceRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WorkplaceServiceTest {

    @Mock
    private WorkplaceRepository repository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private WorkplaceService workplaceService;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeName("John Doe");
        employee.setRole("employee");
    }

    @Test
    void registerWorkplace_shouldReturnError_whenFieldsAreInvalid() {
        // Arrange
        WorkplaceRegistrationDTO invalidNameDTO = new WorkplaceRegistrationDTO();
        invalidNameDTO.setWorkplaceName("");
        invalidNameDTO.setHourlyWage(20.0);
        invalidNameDTO.setOvertimeMultiplier(1.5);

        WorkplaceRegistrationDTO invalidHourlyWageDTO = new WorkplaceRegistrationDTO();
        invalidHourlyWageDTO.setWorkplaceName("Office");
        invalidHourlyWageDTO.setHourlyWage(0);
        invalidHourlyWageDTO.setOvertimeMultiplier(1.5);

        WorkplaceRegistrationDTO invalidMultiplierDTO = new WorkplaceRegistrationDTO();
        invalidMultiplierDTO.setWorkplaceName("Office");
        invalidMultiplierDTO.setHourlyWage(20.0);
        invalidMultiplierDTO.setOvertimeMultiplier(-0.5);

        // Act
        ServiceResponse response1 = workplaceService.registerWorkplace(invalidNameDTO);
        ServiceResponse response2 = workplaceService.registerWorkplace(invalidHourlyWageDTO);
        ServiceResponse response3 = workplaceService.registerWorkplace(invalidMultiplierDTO);

        // Assert
        assertFalse(response1.isSuccess());
        assertEquals("Fill the fields correctly. Workplace Name cannot be empty, and Hourly Wage and Overtime Multiplier must be positive.", response1.getMessage());

        assertFalse(response2.isSuccess());
        assertEquals("Fill the fields correctly. Workplace Name cannot be empty, and Hourly Wage and Overtime Multiplier must be positive.", response2.getMessage());

        assertFalse(response3.isSuccess());
        assertEquals("Fill the fields correctly. Workplace Name cannot be empty, and Hourly Wage and Overtime Multiplier must be positive.", response3.getMessage());
    }

    @Test
    void registerWorkplace_shouldRegisterWorkplaceSuccessfully() {
        // Arrange
        WorkplaceRegistrationDTO validDTO = new WorkplaceRegistrationDTO();
        validDTO.setWorkplaceName("Office");
        validDTO.setHourlyWage(25.0);
        validDTO.setOvertimeMultiplier(1.75);
        validDTO.setEmployeeId(1L); // Assuming you have an employee with ID 1


        Workplace savedWorkplace = new Workplace();
        savedWorkplace.setId(1L);
        savedWorkplace.setWorkplaceName("Office");
        savedWorkplace.setHourlyWage(25.0);
        savedWorkplace.setOvertimeMultiplier(1.75);
        savedWorkplace.setEmployee(employee);

        when(employeeRepository.findById(validDTO.getEmployeeId())).thenReturn(Optional.of(employee));

        when(repository.save(any(Workplace.class))).thenReturn(savedWorkplace);

        // Act
        ServiceResponse response = workplaceService.registerWorkplace(validDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Workplace registered successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Office", ((Workplace) response.getData()).getWorkplaceName());
        assertEquals(25.0, ((Workplace) response.getData()).getHourlyWage(), 0.001); 
        assertEquals(1.75, ((Workplace) response.getData()).getOvertimeMultiplier(), 0.001); 

        verify(repository, times(1)).save(any(Workplace.class));
    }

    @Test
    void registerWorkplace_shouldHandleExceptions() {
        // Arrange
        WorkplaceRegistrationDTO validDTO = new WorkplaceRegistrationDTO();
        validDTO.setWorkplaceName("Office");
        validDTO.setHourlyWage(25.0);
        validDTO.setOvertimeMultiplier(1.75);
        validDTO.setEmployeeId(1L); // Assuming you have an employee with ID 1

        when(employeeRepository.findById(validDTO.getEmployeeId())).thenReturn(Optional.of(employee));

        when(repository.save(any(Workplace.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        ServiceResponse response = workplaceService.registerWorkplace(validDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("An error occurred while registering the workplace"));

        verify(repository, times(1)).save(any(Workplace.class));
    }

    @Test
    void getAllWorkplacesByEmployeeId_Success() {
        // Arrange
        Long employeeId = 1L;
        List<Workplace> workplaces = new ArrayList<>();
        workplaces.add(new Workplace()); // Add some dummy workplaces
        workplaces.add(new Workplace());
        when(repository.findAllByEmployeeId(employeeId)).thenReturn(workplaces);

        // Act
        ServiceResponse response = workplaceService.getAllWorkplacesByEmployeeId(employeeId);

        // Assert
        assertEquals(true, response.isSuccess());
        assertEquals("Workplaces retrieved successfully.", response.getMessage());
        assertEquals(workplaces, response.getData());
    }

        @Test
    void getAllWorkplacesByEmployeeId_NoWorkplacesFound() {
        // Arrange
        Long employeeId = 1L;
        List<Workplace> workplaces = new ArrayList<>(); // Empty list
        when(repository.findAllByEmployeeId(employeeId)).thenReturn(workplaces);

        // Act
        ServiceResponse response = workplaceService.getAllWorkplacesByEmployeeId(employeeId);

        // Assert
        assertEquals(false, response.isSuccess());
        assertEquals("No workplaces found for this employee.", response.getMessage());
        assertEquals(null, response.getData()); // Or whatever you return for empty data
    }

    @Test
    void getAllWorkplacesByEmployeeId_ExceptionThrown() {
        // Arrange
        Long employeeId = 1L;
        when(repository.findAllByEmployeeId(employeeId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ServiceResponse response = workplaceService.getAllWorkplacesByEmployeeId(employeeId);

        // Assert
        assertEquals(false, response.isSuccess());
        assertEquals("Error while retrieving workplaces: Database error", response.getMessage());
        assertEquals(null, response.getData());
    }
}