package com.example.healthcare.healthcare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CaughtException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CaughtException(String message) {
        super(message);
    }

    public CaughtException(String message, Throwable cause) {
        super(message, cause);
    }
}
