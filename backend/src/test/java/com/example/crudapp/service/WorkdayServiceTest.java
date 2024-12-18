package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    Workplace workplace = new Workplace();


    private WorkdayDTO validDTO;
    private WorkdayDTO existingWorkdayDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeName("John Doe");
        testEmployee.setRole("employee");
        
        workplace.setId(3L);
        workplace.setWorkplaceName("Office A");

        workday1 = new Workday();
        workday1.setDate(LocalDate.of(2024, 11, 1));
        workday1.setHoursWorked(8);
        workday1.setOvertimeHours(2);
        workday1.setTransportCost(50.0);
        workday1.setEmployee(testEmployee);
        workday1.setWorkplace(workplace);
        workday1.getWorkplace().setWorkplaceName("Office A");

        workday2 = new Workday();
        workday2.setDate(LocalDate.of(2024, 11, 2));
        workday2.setHoursWorked(8);
        workday2.setOvertimeHours(1);
        workday2.setTransportCost(50.0);
        workday2.setEmployee(testEmployee);
        workday2.setWorkplace(workplace);
        workday2.getWorkplace().setWorkplaceName("Office B");

        validDTO = new WorkdayDTO();
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

        Workplace mockWorkplace = new Workplace();
        mockWorkplace.setId(1L);

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
    void testGenerateWorkdayExcelReport() throws IOException {
        when(workdayRepository.findByEmployeeIdAndDateBetween(testEmployee.getId(),
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2)))
                .thenReturn(Arrays.asList(workday1, workday2));

        ByteArrayOutputStream excelFile = workdayService.generateWorkdayExcelReport(testEmployee,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2));

        assertNotNull(excelFile, "O arquivo Excel gerado não deve ser nulo.");

        assertTrue(excelFile.size() > 0, "O arquivo Excel não contém dados.");

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(excelFile.toByteArray());
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            assertEquals(1, workbook.getNumberOfSheets(), "Deve haver uma planilha no arquivo.");
            assertEquals("Workdays", workbook.getSheetAt(0).getSheetName(), "O nome da planilha deve ser 'Workdays'.");

            assertEquals(3, workbook.getSheetAt(0).getPhysicalNumberOfRows(), "Deve haver 3 linhas no arquivo Excel.");
        }
    }
}
