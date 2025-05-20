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
@Table(name = "workplace")
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workplace_name", nullable = false)
    private String workplaceName;

    @Column(name = "hourly_wage")
    private double hourlyWage;

    @Column(name = "overtime_multiplier")
    private double overtimeMultiplier;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Constructors (optional, but good practice)
    public Workplace() {
    }

   public Workplace(String workplaceName, double hourlyWage, double overtimeMultiplier, Employee employee) {
    this.workplaceName = workplaceName;
    this.hourlyWage = hourlyWage;
    this.overtimeMultiplier = overtimeMultiplier;
    this.employee = employee;
}




    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}