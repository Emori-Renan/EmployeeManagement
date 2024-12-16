package com.example.crudapp.dto;

import java.time.LocalDate;

public class WorkdayDTO {
    private Long employeeId;
    private Long workplaceId;
    private LocalDate date;
    private String workplaceName;
    private double hoursWorked;
    private double overtimeHours;
    private double transportCost;

    public WorkdayDTO() {
    }

    public WorkdayDTO(LocalDate date, String workplaceName, double hoursWorked, double overtimeHours, double transportCost) {
        this.date = date;
        this.workplaceName = workplaceName;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
        this.transportCost = transportCost;
    }

    // Getters and Setters

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }
}
