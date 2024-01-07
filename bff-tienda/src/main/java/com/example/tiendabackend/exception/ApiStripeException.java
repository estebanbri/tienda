package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class ApiStripeException extends ApiException {

    private static final String MESSAGE_PREFIX = "Api Strip exception | ";

    public ApiStripeException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.BAD_REQUEST);
    }
}

