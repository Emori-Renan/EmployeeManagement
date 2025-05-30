package com.example.crudapp.service;
import com.example.crudapp.dto.WorkdayDTO;
import com.example.crudapp.exporter.WorkdayExcelExporter;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workday;
import com.example.crudapp.model.Workplace;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WorkdayExcelExporterTest {

    private Employee testEmployee;
    private Workplace workplaceA;
    private Workplace workplaceB;
    private Workday workday1;
    private Workday workday2;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeName("Test Employee");

        workplaceA = new Workplace();
        workplaceA.setId(101L);
        workplaceA.setWorkplaceName("Main Office");

        workplaceB = new Workplace();
        workplaceB.setId(102L);
        workplaceB.setWorkplaceName("Remote Site");

        workday1 = new Workday();
        workday1.setDate(LocalDate.of(2025, 1, 10));
        workday1.setHoursWorked(8);
        workday1.setOvertimeHours(1);
        workday1.setTransportCost(15.0);
        workday1.setEmployee(testEmployee);
        workday1.setWorkplace(workplaceA);

        workday2 = new Workday();
        workday2.setDate(LocalDate.of(2025, 1, 15));
        workday2.setHoursWorked(7);
        workday2.setOvertimeHours(0);
        workday2.setTransportCost(10.0);
        workday2.setEmployee(testEmployee);
        workday2.setWorkplace(workplaceB);
    }

    @Test
    void export_shouldGenerateExcelWithCorrectHeadersAndData() throws IOException {
        // Arrange
        List<Workday> workdaysToExport = Arrays.asList(workday1, workday2);
        WorkdayExcelExporter exporter = new WorkdayExcelExporter(workdaysToExport);

        // Act
        ByteArrayOutputStream outputStream = exporter.export();

        // Assert
        assertNotNull(outputStream, "Output stream should not be null");
        assertTrue(outputStream.size() > 0, "Output stream should contain data");

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            assertEquals(1, workbook.getNumberOfSheets(), "Should have exactly one sheet");
            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet, "Sheet should not be null");
            assertEquals("Workdays", sheet.getSheetName(), "Sheet name should be 'Workdays'");

            // Verify Header Row
            Row headerRow = sheet.getRow(0);
            assertNotNull(headerRow, "Header row should exist");
            assertEquals("Date", headerRow.getCell(0).getStringCellValue());
            assertEquals("Workplace", headerRow.getCell(1).getStringCellValue());
            assertEquals("Hours Worked", headerRow.getCell(2).getStringCellValue());
            assertEquals("Overtime Hours", headerRow.getCell(3).getStringCellValue());
            assertEquals("Transport Cost", headerRow.getCell(4).getStringCellValue());

            // Verify Data Row 1 (workday1)
            Row dataRow1 = sheet.getRow(1);
            assertNotNull(dataRow1, "Data row 1 should exist");
            assertEquals(workday1.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE), dataRow1.getCell(0).getStringCellValue());
            assertEquals(workday1.getWorkplace().getWorkplaceName(), dataRow1.getCell(1).getStringCellValue());
            assertEquals(workday1.getHoursWorked(), dataRow1.getCell(2).getNumericCellValue());
            assertEquals(workday1.getOvertimeHours(), dataRow1.getCell(3).getNumericCellValue());
            assertEquals(workday1.getTransportCost(), dataRow1.getCell(4).getNumericCellValue(), 0.001); // Delta for double comparison

            // Verify Data Row 2 (workday2)
            Row dataRow2 = sheet.getRow(2);
            assertNotNull(dataRow2, "Data row 2 should exist");
            assertEquals(workday2.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE), dataRow2.getCell(0).getStringCellValue());
            assertEquals(workday2.getWorkplace().getWorkplaceName(), dataRow2.getCell(1).getStringCellValue());
            assertEquals(workday2.getHoursWorked(), dataRow2.getCell(2).getNumericCellValue());
            assertEquals(workday2.getOvertimeHours(), dataRow2.getCell(3).getNumericCellValue());
            assertEquals(workday2.getTransportCost(), dataRow2.getCell(4).getNumericCellValue(), 0.001);

            assertEquals(workdaysToExport.size() + 1, sheet.getPhysicalNumberOfRows(), "Number of rows should be data rows + header");
        }
    }

    @Test
    void export_shouldHandleEmptyList() throws IOException {
        // Arrange
        List<Workday> emptyList = Collections.emptyList();
        WorkdayExcelExporter exporter = new WorkdayExcelExporter(emptyList);

        // Act
        ByteArrayOutputStream outputStream = exporter.export();

        // Assert
        assertNotNull(outputStream, "Output stream should not be null");
        assertTrue(outputStream.size() > 0, "Output stream should contain data (at least header)");

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(1, sheet.getPhysicalNumberOfRows(), "Should only have the header row when list is empty");
            // Verify header row still exists
            assertNotNull(sheet.getRow(0), "Header row must exist even for empty data");
        }
    }

    @Test
    void export_shouldHandleSingleWorkday() throws IOException {
        // Arrange
        List<Workday> singleWorkdayList = Collections.singletonList(workday1);
        WorkdayExcelExporter exporter = new WorkdayExcelExporter(singleWorkdayList);

        // Act
        ByteArrayOutputStream outputStream = exporter.export();

        // Assert
        assertNotNull(outputStream);
        assertTrue(outputStream.size() > 0);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(2, sheet.getPhysicalNumberOfRows(), "Should have header + one data row");
            assertEquals(workday1.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE), sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals(workday1.getWorkplace().getWorkplaceName(), sheet.getRow(1).getCell(1).getStringCellValue());
        }
    }
}