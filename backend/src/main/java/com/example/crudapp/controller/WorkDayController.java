package com.example.crudapp.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkdayDTO;
import com.example.crudapp.service.WorkdayService;

@RestController
public class WorkDayController {
    private final WorkdayService workdayService;

    public WorkDayController(WorkdayService workdayService) {
        this.workdayService = workdayService;
    }

    @PostMapping("/workday/register")
    public ResponseEntity<ServiceResponse> registerWorkday(@RequestBody WorkdayDTO workdayDTO){
        ServiceResponse response = workdayService.registerWorkday(workdayDTO);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
   @GetMapping("/workdays/{employeeId}/filtered")
    public ResponseEntity<ServiceResponse> getWorkdaysFiltered(
            @PathVariable Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long workplaceId) {

        ServiceResponse response = workdayService.getWorkdaysFiltered(employeeId, startDate, endDate, workplaceId);
        return ResponseEntity.ok(response);
    }

}
