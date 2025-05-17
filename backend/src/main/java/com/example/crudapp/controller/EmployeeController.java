package com.example.crudapp.controller;

import java.security.Provider.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.dto.EmployeeRegistrationDTO;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> registerEmployee(@RequestBody EmployeeRegistrationDTO employee) {
        ServiceResponse response = employeeService.registerEmployee(employee);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ServiceResponse> getEmployees(@RequestParam String username) {
        ServiceResponse response = employeeService.getEmployees(username);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getEmployeeById(@PathVariable Long id) {
        ServiceResponse response = employeeService.getEmployeeById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRegistrationDTO employee) {
        ServiceResponse response = employeeService.updateEmployee(id, employee);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
}
