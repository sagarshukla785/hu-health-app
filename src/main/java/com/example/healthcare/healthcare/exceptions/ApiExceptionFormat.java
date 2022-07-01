package com.example.healthcare.healthcare.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ApiExceptionFormat {
    private String errorMessage;

    public ApiExceptionFormat(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }

    public void setMessage(String message) {
        this.errorMessage = message;
    }
}
