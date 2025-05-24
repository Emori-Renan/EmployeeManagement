package com.example.crudapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkplaceRegistrationDTO;
import com.example.crudapp.service.WorkplaceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/workplace")
public class WorkplaceController {
    private final WorkplaceService service;

    public WorkplaceController(WorkplaceService service ){
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> postMethodName(@RequestBody WorkplaceRegistrationDTO workplaceDTO) {
        ServiceResponse response = service.registerWorkplace(workplaceDTO);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ServiceResponse> getAllWorkplacesByEmployeeId(@org.springframework.web.bind.annotation.PathVariable Long employeeId) {
        ServiceResponse response = service.getAllWorkplacesByEmployeeId(employeeId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
