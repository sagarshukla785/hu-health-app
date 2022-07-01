package com.example.healthcare.healthcare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {CaughtException.class})
    public ResponseEntity<Object> handleAPIRequestException(CaughtException ex){
        ApiExceptionFormat apiExceptionFormat = new ApiExceptionFormat(ex.getMessage());
        return new ResponseEntity<>(apiExceptionFormat, HttpStatus.BAD_REQUEST);
    }
}
