package com.example.crudapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkplaceRegistrationDTO;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.WorkplaceRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WorkplaceServiceTest {

    @Mock
    private WorkplaceRepository repository;

    @InjectMocks
    private WorkplaceService workplaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerWorkplace_shouldReturnError_whenFieldsAreInvalid() {
        // Arrange
        WorkplaceRegistrationDTO invalidNameDTO = new WorkplaceRegistrationDTO();
        invalidNameDTO.setWorkplaceName("");
        invalidNameDTO.setDailySalary(100);

        WorkplaceRegistrationDTO invalidSalaryDTO = new WorkplaceRegistrationDTO();
        invalidSalaryDTO.setWorkplaceName("Office");
        invalidSalaryDTO.setDailySalary(-10);

        // Act
        ServiceResponse response1 = workplaceService.registerWorkplace(invalidNameDTO);
        ServiceResponse response2 = workplaceService.registerWorkplace(invalidSalaryDTO);

        // Assert
        assertFalse(response1.isSuccess());
        assertEquals("Fill the fields correctly.", response1.getMessage());

        assertFalse(response2.isSuccess());
        assertEquals("Fill the fields correctly.", response2.getMessage());
    }

    @Test
    void registerWorkplace_shouldRegisterWorkplaceSuccessfully() {
        // Arrange
        WorkplaceRegistrationDTO validDTO = new WorkplaceRegistrationDTO();
        validDTO.setWorkplaceName("Office");
        validDTO.setDailySalary(150);

        Workplace savedWorkplace = new Workplace();
        savedWorkplace.setId(1L);
        savedWorkplace.setWorkplaceName("Office");
        savedWorkplace.setDailySalary(150);

        when(repository.save(any(Workplace.class))).thenReturn(savedWorkplace);

        // Act
        ServiceResponse response = workplaceService.registerWorkplace(validDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Employee registered successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Office", ((Workplace) response.getData()).getWorkplaceName());
        assertEquals(150, ((Workplace) response.getData()).getDailySalary());

        verify(repository, times(1)).save(any(Workplace.class));
    }

    @Test
    void registerWorkplace_shouldHandleExceptions() {
        // Arrange
        WorkplaceRegistrationDTO validDTO = new WorkplaceRegistrationDTO();
        validDTO.setWorkplaceName("Office");
        validDTO.setDailySalary(150);

        when(repository.save(any(Workplace.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        ServiceResponse response = workplaceService.registerWorkplace(validDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("An error occurred while registering the employee"));

        verify(repository, times(1)).save(any(Workplace.class));
    }
}

