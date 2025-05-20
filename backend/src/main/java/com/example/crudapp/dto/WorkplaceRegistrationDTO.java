package com.example.crudapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class WorkplaceRegistrationDTO {

    @NotBlank(message = "Workplace Name is mandatory")
    private String workplaceName;

    @Positive(message = "Hourly Wage must be a positive value")
    private double hourlyWage;

    @Positive(message = "Overtime Multiplier must be a positive value")
    private double overtimeMultiplier;

    @NotBlank(message = "Employee ID is mandatory")
    private Long employeeId;

    // Getters and setters

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public double getOvertimeMultiplier() {
        return overtimeMultiplier;
    }

    public void setOvertimeMultiplier(double overtimeMultiplier) {
        this.overtimeMultiplier = overtimeMultiplier;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}