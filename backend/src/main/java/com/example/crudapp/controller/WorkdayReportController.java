package com.example.crudapp.controller;

import com.example.crudapp.model.Employee; // Assuming you have this model
import com.example.crudapp.service.WorkdayService; // Import your service

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@RestController
public class WorkdayReportController {

    private final WorkdayService workdayService;

    // Constructor Injection for the WorkdayService
    public WorkdayReportController(WorkdayService workdayService) {
        this.workdayService = workdayService;
    }

    @GetMapping("/workday-report/{employeeId}")
    public ResponseEntity<byte[]> downloadWorkdayReport(@PathVariable Long employeeId,
                                                         @RequestParam String startDate,
                                                         @RequestParam String endDate,
                                                         @RequestParam(required = false) String workplace) throws IOException {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Employee employee = new Employee();
        employee.setId(employeeId);

        ByteArrayOutputStream report = workdayService.generateWorkdayExcelReport(employee, start, end, workplace);

        HttpHeaders headers = new HttpHeaders();
        String filename = "workday_report";
        if (workplace != null && !workplace.isEmpty()) {
            filename += "_" + workplace.replaceAll("[^a-zA-Z0-9.-]", "_");
        }
        filename += ".xlsx";

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(report.toByteArray());
    }
}