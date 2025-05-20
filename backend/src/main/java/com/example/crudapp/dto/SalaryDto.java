package com.example.crudapp.dto;

public class SalaryDto {

    private Long id;
    private Long employeeId; // ID do funcionário
    private String monthYear;
    private Integer totalDaysWorked;
    private Double totalHours;
    private Double overtimeHours;
    private Double totalTransportCost;
    private Double finalSalary;

    // Construtores
    public SalaryDto() {
    }

    public SalaryDto(Long id, Long employeeId, String monthYear, Integer totalDaysWorked,
                     Double totalHours, Double overtimeHours, Double totalTransportCost,
                     Double finalSalary) {
        this.id = id;
        this.employeeId = employeeId;
        this.monthYear = monthYear;
        this.totalDaysWorked = totalDaysWorked;
        this.totalHours = totalHours;
        this.overtimeHours = overtimeHours;
        this.totalTransportCost = totalTransportCost;
        this.finalSalary = finalSalary;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Integer getTotalDaysWorked() {
        return totalDaysWorked;
    }

    public void setTotalDaysWorked(Integer totalDaysWorked) {
        this.totalDaysWorked = totalDaysWorked;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Double getTotalTransportCost() {
        return totalTransportCost;
    }

    public void setTotalTransportCost(Double totalTransportCost) {
        this.totalTransportCost = totalTransportCost;
    }

    public Double getFinalSalary() {
        return finalSalary;
    }

    public void setFinalSalary(Double finalSalary) {
        this.finalSalary = finalSalary;
    }
}