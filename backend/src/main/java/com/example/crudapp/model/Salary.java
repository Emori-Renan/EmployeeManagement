package com.example.crudapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "salary")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;  // Referência ao funcionário

    @Column(name = "month_year", nullable = false, length = 7)
    private String monthYear;  // Mês e ano do cálculo (ex: 2024-11)

    @Column(name = "total_days_worked", nullable = false)
    private Integer totalDaysWorked;  // Total de dias trabalhados

    @Column(name = "total_hours", nullable = false)
    private Double totalHours;  // Total de horas trabalhadas no mês

    @Column(name = "overtime_hours", nullable = false)
    private Double overtimeHours;  // Total de horas extras

    @Column(name = "total_transport_cost", nullable = false)
    private Double totalTransportCost;  // Custo total de transporte

    @Column(name = "final_salary", nullable = false)
    private Double finalSalary;  // Salário final calculado

    // Construtores, getters e setters
    public Salary() {
    }

    public Salary(Employee employee, String monthYear, Integer totalDaysWorked, 
                  Double totalHours, Double overtimeHours, Double totalTransportCost, 
                  Double finalSalary) {
        this.employee = employee;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
