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


public class WorkdayExcelExporter {

    private Workbook workbook;
    private Sheet sheet;
    private List<Workday> workdays;

    public WorkdayExcelExporter(List<Workday> workdays) {
        this.workdays = workdays;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Workdays");
    }


    private void writeHeaderLine() {
        Row headerRow = sheet.createRow(0); 

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); 
        style.setFont(font);

        createCell(headerRow, 0, "Date", style);
        createCell(headerRow, 1, "Workplace", style);
        createCell(headerRow, 2, "Hours Worked", style);
        createCell(headerRow, 3, "Overtime Hours", style);
        createCell(headerRow, 4, "Transport Cost", style);
    }


    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount); 

        if (value instanceof LocalDate localDate) {
            cell.setCellValue(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else if (value instanceof Integer integer) {
            cell.setCellValue(integer);
        } else if (value instanceof Double doubleValue) {
            cell.setCellValue(doubleValue);
        } else if (value != null) {
            cell.setCellValue(value.toString());
        } else {
            cell.setCellValue(""); 
        }

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
        writeHeaderLine();
        writeDataLines(); 

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close(); 
        return outputStream;
    }
}
