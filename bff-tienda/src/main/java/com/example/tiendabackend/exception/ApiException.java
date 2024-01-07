package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private int statusCode;

    public ApiException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode.value();
    }

    public int statusCode() {
        return statusCode;
    }

}
