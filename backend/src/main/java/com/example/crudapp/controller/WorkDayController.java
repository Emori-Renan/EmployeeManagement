package com.example.crudapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.model.Employee;
import com.example.crudapp.service.WorkdayService;

@RestController
public class WorkDayController {
    @Autowired
    private WorkdayService workdayService;

    @GetMapping("/workday-report/{employeeId}")
    public ResponseEntity<byte[]> downloadWorkdayReport(@PathVariable Long employeeId,
            @RequestParam String startDate,
            @RequestParam String endDate) throws IOException {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Employee employee = new Employee();
        employee.setId(employeeId);

        ByteArrayOutputStream report = workdayService.generateWorkdayExcelReport(employee, start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=workday_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(report.toByteArray());
    }

}
