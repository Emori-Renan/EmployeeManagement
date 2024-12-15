package com.example.crudapp.dto;

import jakarta.validation.constraints.NotBlank;

public class WorkplaceRegistrationDTO {
    @NotBlank(message = "Workplace Name is mandatory")
    private String workplaceName;

    @NotBlank(message = "Name is mandatory")
    private int dailySalary;

    //getters and setters

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public int getDailySalary() {
        return dailySalary;
    }

    public void setDailySalary(int dailySalary) {
        this.dailySalary = dailySalary;
    }

}
