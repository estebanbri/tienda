package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {

    private static final String MESSAGE_PREFIX = "Unauthorized access | ";

    public AuthenticationException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.UNAUTHORIZED);
    }
}
