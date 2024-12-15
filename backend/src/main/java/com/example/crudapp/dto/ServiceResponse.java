package com.example.crudapp.dto;

public class ServiceResponse {

    private boolean success;
    private String message;
    private Object data;

    public ServiceResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ServiceResponse(boolean success, String message) {
        this(success, message, null);
    }

     // Factory methods for success and error responses
     public static ServiceResponse success(String message, Object data) {
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
