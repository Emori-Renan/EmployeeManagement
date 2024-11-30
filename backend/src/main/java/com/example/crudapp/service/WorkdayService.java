package com.example.crudapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crudapp.dto.WorkdayDTO;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workday;
import com.example.crudapp.repository.WorkdayRepository;

@Service
public class WorkdayService {
    @Autowired
    private WorkdayRepository workdayRepository;

    public ByteArrayOutputStream generateWorkdayExcelReport(Employee employee, LocalDate startDate, LocalDate endDate)
            throws IOException {
        List<Workday> workdays = workdayRepository.findByEmployeeIdAndDateBetween(employee.getId(), startDate, endDate);

        List<WorkdayDTO> workdayDTOs = workdays.stream()
                .map(workday -> new WorkdayDTO(
                        workday.getDate(),
                        workday.getWorkplace().getName(),
                        workday.getHoursWorked(),
                        workday.getOvertimeHours(),
                        workday.getTransportCost()))
                .collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Workdays");

        Row headerRow = (sheet).createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Workplace");
        headerRow.createCell(2).setCellValue("Hours Worked");
        headerRow.createCell(3).setCellValue("Overtime Hours");
        headerRow.createCell(4).setCellValue("Transport Cost");

        int rowNum = 1;
        for (WorkdayDTO workdayDTO : workdayDTOs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(workdayDTO.getDate().toString());
            row.createCell(1).setCellValue(workdayDTO.getWorkplaceName());
            row.createCell(2).setCellValue(workdayDTO.getHoursWorked());
            row.createCell(3).setCellValue(workdayDTO.getOvertimeHours());
            row.createCell(4).setCellValue(workdayDTO.getTransportCost());
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
    }
}
