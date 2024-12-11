package com.example.crudapp.dto;

public class UserRegistrationResult {
    private boolean success;
    private String message;

    public UserRegistrationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

     // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
