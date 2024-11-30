package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workday;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.WorkdayRepository;

@ExtendWith(MockitoExtension.class)
class WorkdayServiceTest {

    @InjectMocks
    private WorkdayService workdayService;

    @Mock
    private WorkdayRepository workdayRepository;

    private Employee testEmployee;
    private Workday workday1;
    private Workday workday2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setName("John Doe");
        testEmployee.setRole("employee");

        workday1 = new Workday();
        workday1.setDate(LocalDate.of(2024, 11, 1));
        workday1.setHoursWorked(8);
        workday1.setOvertimeHours(2);
        workday1.setTransportCost(50.0);
        workday1.setEmployee(testEmployee);
        workday1.setWorkplace(new Workplace());
        workday1.getWorkplace().setName("Office A");

        workday2 = new Workday();
        workday2.setDate(LocalDate.of(2024, 11, 2));
        workday2.setHoursWorked(8);
        workday2.setOvertimeHours(1);
        workday2.setTransportCost(50.0);
        workday2.setEmployee(testEmployee);
        workday2.setWorkplace(new Workplace());
        workday2.getWorkplace().setName("Office B");
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
