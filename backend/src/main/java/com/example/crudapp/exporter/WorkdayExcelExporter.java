package com.example.crudapp.exporter;

import com.example.crudapp.model.Workday;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * WorkdayExcelExporter is responsible for generating an Excel workbook
 * containing workday data. It takes a list of Workday objects and
 * transforms them into a structured .xlsx file.
 *
 * This class uses Apache POI library for Excel generation.
 */
// @Component // Uncomment if you want Spring to manage this as a bean
public class WorkdayExcelExporter {

    private Workbook workbook;
    private Sheet sheet;
    private List<Workday> workdays;

    /**
     * Constructs a new WorkdayExcelExporter.
     * Initializes an XSSFWorkbook and creates a sheet named "Workdays".
     *
     * @param workdays The list of Workday objects to be exported to Excel.
     */
    public WorkdayExcelExporter(List<Workday> workdays) {
        this.workdays = workdays;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Workdays");
    }

    /**
     * Writes the header row to the Excel sheet.
     * Sets up cell styles for the header (bold font, larger size).
     */
    private void writeHeaderLine() {
        Row headerRow = sheet.createRow(0); // Header is always the first row (row 0)

        // Create a cell style for the header
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true); // Make font bold
        font.setFontHeightInPoints((short) 14); // Set font size to 14 points
        style.setFont(font); // Apply font to style

        // Create header cells and set their values
        createCell(headerRow, 0, "Date", style);
        createCell(headerRow, 1, "Workplace", style);
        createCell(headerRow, 2, "Hours Worked", style);
        createCell(headerRow, 3, "Overtime Hours", style);
        createCell(headerRow, 4, "Transport Cost", style);
    }

    /**
     * Helper method to create a cell, set its value, and apply a style.
     * Handles different data types (LocalDate, Integer, Double, String).
     *
     * @param row The row to which the cell belongs.
     * @param columnCount The column index for the new cell.
     * @param value The value to be set in the cell.
     * @param style The CellStyle to apply to the cell.
     */
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount); // Create the cell at the specified column

        // Set cell value based on its type
        if (value instanceof LocalDate localDate) {
            cell.setCellValue(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else if (value instanceof Integer integer) {
            cell.setCellValue(integer);
        } else if (value instanceof Double doubleValue) {
            cell.setCellValue(doubleValue);
        } else if (value != null) {
            // For other types, convert to String. Handles potential null values gracefully.
            cell.setCellValue(value.toString());
        } else {
            cell.setCellValue(""); // Set empty string for null values
        }

        // Apply the provided style if it's not null
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void writeDataLines() {
        int rowCount = 1; 

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        for (Workday workday : workdays) {
            Row row = sheet.createRow(rowCount++); 
            int columnCount = 0;

            createCell(row, columnCount++, workday.getDate(), style);
            createCell(row, columnCount++, (workday.getWorkplace() != null ? workday.getWorkplace().getWorkplaceName() : "N/A"), style);
            createCell(row, columnCount++, workday.getHoursWorked(), style);
            createCell(row, columnCount++, workday.getOvertimeHours(), style);
            createCell(row, columnCount++, workday.getTransportCost(), style);
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public ByteArrayOutputStream export() throws IOException {
        writeHeaderLine(); // Write the header row
        writeDataLines();  // Write all data rows

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream); // Write the workbook content to the output stream
        workbook.close(); // Close the workbook to release resources
        return outputStream;
    }
}
