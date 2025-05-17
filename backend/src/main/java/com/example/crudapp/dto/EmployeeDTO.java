package com.example.crudapp.dto;

public class EmployeeDTO {  
    private Long id;
    private String employeeName;
    private String role;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String employeeName, String role) {
        this.id = id;
        this.employeeName = employeeName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
