package com.example.crudapp.dto;

import com.example.crudapp.model.Employee;

public class ServiceResponse {

    private boolean success;
    private String message;
    private Employee data;

    public ServiceResponse(boolean success, String message, Employee data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ServiceResponse(boolean success, String message) {
        this(success, message, null);
    }

     // Factory methods for success and error responses
     public static ServiceResponse success(String message, Employee data) {
        return new ServiceResponse(true, message, data);
    }

    public static ServiceResponse error(String message) {
        return new ServiceResponse(false, message);
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
