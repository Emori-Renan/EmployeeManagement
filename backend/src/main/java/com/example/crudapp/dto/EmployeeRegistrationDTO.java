package com.example.crudapp.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeRegistrationDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Name is mandatory")
    private String employeeName;

    @NotBlank(message = "Role is mandatory")
    private String role;  // Should be either "ADMIN" or "EMPLOYEE"

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
}
