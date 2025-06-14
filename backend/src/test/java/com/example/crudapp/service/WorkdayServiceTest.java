package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkdayDTO;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workday;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.WorkdayRepository;
import com.example.crudapp.repository.WorkplaceRepository;

@ExtendWith(MockitoExtension.class)
class WorkdayServiceTest {

    @InjectMocks
    private WorkdayService workdayService;

    @Mock
    private WorkdayRepository workdayRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private WorkplaceRepository workplaceRepository;

    private Employee testEmployee;
    private Workday workday1;
    private Workday workday2;
    private Workplace workplaceA;
    private Workplace workplaceB; 

    private WorkdayDTO validDTO;
    private WorkdayDTO existingWorkdayDTO;

    @BeforeEach
    void setUp() {

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeName("John Doe");
        testEmployee.setRole("employee");

        workplaceA = new Workplace();
        workplaceA.setId(1L); 
        workplaceA.setWorkplaceName("Office A");

        workplaceB = new Workplace();
        workplaceB.setId(2L);
        workplaceB.setWorkplaceName("Office B");

        workday1 = new Workday();
        workday1.setDate(LocalDate.of(2024, 11, 1));
        workday1.setHoursWorked(8);
        workday1.setOvertimeHours(2);
        workday1.setTransportCost(50.0);
        workday1.setEmployee(testEmployee);
        workday1.setWorkplace(workplaceA);

        workday2 = new Workday();
        workday2.setDate(LocalDate.of(2024, 11, 2));
        workday2.setHoursWorked(8);
        workday2.setOvertimeHours(1);
        workday2.setTransportCost(50.0);
        workday2.setEmployee(testEmployee);
        workday2.setWorkplace(workplaceB); 

        validDTO = new WorkdayDTO();
        validDTO.setWorkplaceName("Office A"); 
        validDTO.setEmployeeId(1L);
        validDTO.setWorkplaceId(1L);
        validDTO.setDate(LocalDate.now());
        validDTO.setHoursWorked(8);
        validDTO.setOvertimeHours(2);
        validDTO.setTransportCost(15.50);

        existingWorkdayDTO = new WorkdayDTO();
        existingWorkdayDTO.setEmployeeId(1L);
        existingWorkdayDTO.setWorkplaceId(1L);
        existingWorkdayDTO.setDate(LocalDate.now()); 
    }


    @Test
    void registerWorkday_shouldRegisterWorkdaySuccessfully() {
        // Arrange
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setEmployeeName("John Doe");
        mockEmployee.setRole("employee");

        Workplace mockWorkplace = new Workplace();
        mockWorkplace.setId(1L);
        mockWorkplace.setWorkplaceName("Office A"); 

        Workday savedWorkday = new Workday();
        savedWorkday.setId(1L);
        savedWorkday.setEmployee(mockEmployee);
        savedWorkday.setWorkplace(mockWorkplace);
        savedWorkday.setDate(validDTO.getDate());
        savedWorkday.setHoursWorked(validDTO.getHoursWorked());
        savedWorkday.setOvertimeHours(validDTO.getOvertimeHours());
        savedWorkday.setTransportCost(validDTO.getTransportCost());

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
        when(workplaceRepository.findById(1L)).thenReturn(Optional.of(mockWorkplace));
        when(workdayRepository.findByEmployeeIdAndWorkplaceIdAndDate(1L, 1L, validDTO.getDate()))
                .thenReturn(Optional.empty()); 
        when(workdayRepository.save(any(Workday.class))).thenReturn(savedWorkday);

        // Act
        ServiceResponse response = workdayService.registerWorkday(validDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Workday registered successfully", response.getMessage());
        assertNotNull(response.getData());
        Workday result = (Workday) response.getData();
        assertEquals(1L, result.getEmployee().getId());
        assertEquals(1L, result.getWorkplace().getId());
        assertEquals(validDTO.getDate(), result.getDate());
        assertEquals(validDTO.getHoursWorked(), result.getHoursWorked());
        assertEquals(validDTO.getOvertimeHours(), result.getOvertimeHours());
        assertEquals(validDTO.getTransportCost(), result.getTransportCost());

        verify(employeeRepository, times(1)).findById(1L);
        verify(workplaceRepository, times(1)).findById(1L);
        verify(workdayRepository, times(1)).findByEmployeeIdAndWorkplaceIdAndDate(1L, 1L, validDTO.getDate());
        verify(workdayRepository, times(1)).save(any(Workday.class));
    }

    @Test
    void registerWorkday_shouldReturnErrorIfWorkdayAlreadyExists() {
        // Arrange
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1L);
        Workplace mockWorkplace = new Workplace();
        mockWorkplace.setId(1L);

        Workday existingWorkday = new Workday();
        existingWorkday.setEmployee(mockEmployee);
        existingWorkday.setWorkplace(mockWorkplace);
        existingWorkday.setDate(existingWorkdayDTO.getDate());

        when(workdayRepository.findByEmployeeIdAndWorkplaceIdAndDate(
                existingWorkdayDTO.getEmployeeId(),
                existingWorkdayDTO.getWorkplaceId(),
                existingWorkdayDTO.getDate()))
                .thenReturn(Optional.of(existingWorkday));

        // Act
        ServiceResponse response = workdayService.registerWorkday(existingWorkdayDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Workday already exists for this employee at this workplace on this date.", response.getMessage());
        verify(employeeRepository, times(0)).findById(any(Long.class)); 
        verify(workplaceRepository, times(0)).findById(any(Long.class)); 
        verify(workdayRepository, times(1)).findByEmployeeIdAndWorkplaceIdAndDate(
                existingWorkdayDTO.getEmployeeId(),
                existingWorkdayDTO.getWorkplaceId(),
                existingWorkdayDTO.getDate());
        verify(workdayRepository, times(0)).save(any(Workday.class)); 
    }

    @Test
    void registerWorkday_shouldReturnErrorIfEmployeeNotFound() {
        // Arrange
        when(workdayRepository.findByEmployeeIdAndWorkplaceIdAndDate(any(Long.class), any(Long.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(employeeRepository.findById(validDTO.getEmployeeId())).thenReturn(Optional.empty()); 

        // Act
        ServiceResponse response = workdayService.registerWorkday(validDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Employee not found"));
        verify(employeeRepository, times(1)).findById(validDTO.getEmployeeId());
        verify(workplaceRepository, times(0)).findById(any(Long.class)); 
        verify(workdayRepository, times(0)).save(any(Workday.class));
    }

    @Test
    void registerWorkday_shouldReturnErrorIfWorkplaceNotFound() {
        // Arrange
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1L);

        when(workdayRepository.findByEmployeeIdAndWorkplaceIdAndDate(any(Long.class), any(Long.class), any(LocalDate.class)))
                .thenReturn(Optional.empty()); 
        when(employeeRepository.findById(validDTO.getEmployeeId())).thenReturn(Optional.of(mockEmployee));
        when(workplaceRepository.findById(validDTO.getWorkplaceId())).thenReturn(Optional.empty());

        // Act
        ServiceResponse response = workdayService.registerWorkday(validDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Workplace not found"));
        verify(employeeRepository, times(1)).findById(validDTO.getEmployeeId());
        verify(workplaceRepository, times(1)).findById(validDTO.getWorkplaceId());
        verify(workdayRepository, times(0)).save(any(Workday.class)); 
    }
    
        @Test
void testGetWorkdaysWithFilters() {
    // Arrange
    LocalDate start = LocalDate.of(2024, 1, 1);
    LocalDate end = LocalDate.of(2024, 1, 31);
    Long workplaceId = 1L;

    when(workdayRepository.findByEmployeeIdAndDateBetweenAndWorkplaceId(
        testEmployee.getId(), start, end, workplaceId
    )).thenReturn(List.of(workday1)); // only one matching

    // Act
    ServiceResponse response = workdayService.getWorkdaysFiltered(
        testEmployee.getId(), start, end, workplaceId
    );

    // Assert
    assertTrue(response.isSuccess());
    assertEquals("Workdays retrieved successfully", response.getMessage());
    assertNotNull(response.getData());

    List<?> result = (List<?>) response.getData();
    assertEquals(1, result.size());
}

}